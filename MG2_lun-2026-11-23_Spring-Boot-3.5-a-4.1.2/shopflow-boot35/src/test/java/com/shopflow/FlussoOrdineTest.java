package com.shopflow;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;

// Flusso completo su HTTP: ordini -> magazzino (scarico, stessa transazione) -> pagamenti -> evento -> magazzino
// Client di test: TestRestTemplate (Boot 3.5), con server su porta casuale
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "shopflow.dati-demo=false")
@Import(TestcontainersConfiguration.class)
class FlussoOrdineTest {

    @Autowired TestRestTemplate rest;
    @Autowired JdbcClient jdbc;

    @BeforeEach
    void articoli() {
        jdbc.sql("insert into articoli(sku, descrizione, giacenza, versione) values ('ABC-1', 'Tazza', 10, 0)").update();
    }

    @Test
    @SuppressWarnings("unchecked")
    void ordineCreatoScaricaLoStockEPreparaLaSpedizione() {
        var creazione = rest.postForEntity("/ordini", richiesta("ABC-1", 3, "12.00"), Map.class);
        assertThat(creazione.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var id = creazione.getBody().get("valore");

        // scarico nella stessa transazione dell'ordine (MG5)
        assertThat(giacenza("ABC-1")).isEqualTo(7);
        // evento OrdineConfermato gestito dopo il commit (MG4) e registro eventi completato (MG5)
        assertThat(jdbc.sql("select count(*) from spedizioni_da_preparare").query(Long.class).single()).isOne();
        assertThat(jdbc.sql("select count(*) from event_publication").query(Long.class).single())
                .as("con completion-mode=archive gli eventi completati escono da event_publication").isZero();
        assertThat(jdbc.sql("select count(*) from event_publication_archive").query(Long.class).single()).isOne();

        // lettura v2 dello stesso ordine
        var intestazioni = new HttpHeaders();
        intestazioni.add("X-API-Version", "2.0");
        var v2 = rest.exchange("/ordini/{id}", HttpMethod.GET, new HttpEntity<>(intestazioni), Map.class, id);
        assertThat(v2.getStatusCode()).isEqualTo(HttpStatus.OK);
        var totale = (Map<String, Object>) v2.getBody().get("totale");
        assertThat(totale.get("importo")).isEqualTo(36.0);
        assertThat(totale.get("valuta")).isEqualTo("EUR");
    }

    @Test
    void stockInsufficienteDa409ENonLasciaTracce() {
        var risposta = rest.postForEntity("/ordini", richiesta("ABC-1", 50, "12.00"), Map.class);

        assertThat(risposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(risposta.getBody()).containsEntry("sku", "ABC-1");
        assertThat(giacenza("ABC-1")).isEqualTo(10);
        assertThat(jdbc.sql("select count(*) from ordini").query(Long.class).single()).isZero();
    }

    @Test
    void richiestaSenzaRigheDa400() {
        var risposta = rest.postForEntity("/ordini", Map.of("idCliente", "C-1", "righe", List.of()), Map.class);
        assertThat(risposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Il mapper rifiuta i campi sconosciuti (vedi comune.web.JsonConfig)
    @Test
    void campoSconosciutoNellaRichiestaDa400() {
        var corpo = new java.util.HashMap<>(richiesta("ABC-1", 1, "12.00"));
        corpo.put("scontoSegreto", 99);
        var risposta = rest.postForEntity("/ordini", corpo, Map.class);

        assertThat(risposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(giacenza("ABC-1")).isEqualTo(10);
    }

    @AfterEach
    void pulisci() {
        for (var tabella : new String[] {"righe_ordine", "ordini", "tentativi_pagamento", "spedizioni_da_preparare",
                "articoli", "event_publication", "event_publication_archive"})
            jdbc.sql("delete from " + tabella).update();
    }

    private int giacenza(String sku) {
        return jdbc.sql("select giacenza from articoli where sku = ?").param(sku).query(Integer.class).single();
    }

    private static Map<String, Object> richiesta(String sku, int quantita, String prezzo) {
        return Map.of("idCliente", "C-1", "righe", List.of(Map.of(
                "sku", sku, "quantita", quantita,
                "prezzo", Map.of("importo", prezzo, "valuta", "EUR"))));
    }
}
