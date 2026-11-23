package com.shopflow.ordini.interno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import com.shopflow.TestcontainersConfiguration;
import com.shopflow.comune.Denaro;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.ordini.spi.ScaricoMagazzino;
import com.shopflow.ordini.spi.StockInsufficienteException;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// MG5 Base: niente @Transactional sulla classe di test, altrimenti il rollback vero non si vede
@SpringBootTest(properties = "shopflow.dati-demo=false")
@Import(TestcontainersConfiguration.class)
class CreaOrdineServiceTest {

    @Autowired CreaOrdineService servizio;
    @Autowired JdbcClient jdbc;
    @MockitoBean ScaricoMagazzino magazzino;      // già @MockitoBean: esiste dal Framework 6.2, quindi anche su Boot 3.5

    @Test
    void ordineNonSalvatoSeScaricoFallisce() {
        doThrow(new StockInsufficienteException("ABC-1"))
            .when(magazzino).scarica(anyString(), anyInt());

        assertThatThrownBy(() -> servizio.esegui(richiesta("10.00")))
            .isInstanceOf(StockInsufficienteException.class);

        // Query diretta sul database: il rollback c'è stato davvero
        assertThat(conta("ordini")).isZero();
    }

    @Test
    void pagamentoRifiutatoAnnullaOrdineMaIlTentativoResta() {
        // 2000 EUR supera la soglia del gestore simulato (1000)
        assertThatThrownBy(() -> servizio.esegui(richiesta("2000.00")))
            .isInstanceOf(PagamentoRifiutatoException.class);

        assertThat(conta("ordini")).isZero();                    // rollbackFor = Exception.class
        assertThat(conta("tentativi_pagamento")).isOne();        // REQUIRES_NEW: commit indipendente
    }

    @Test
    void ordineValidoVieneSalvatoComePagato() throws Exception {
        var id = servizio.esegui(richiesta("10.00"));

        var stato = jdbc.sql("select stato from ordini where id = ?").param(id.valore()).query(String.class).single();
        assertThat(stato).isEqualTo("PAGATO");
    }

    @AfterEach
    void pulisci() {                              // nessun rollback automatico: si ripulisce a mano
        jdbc.sql("delete from righe_ordine").update();
        jdbc.sql("delete from ordini").update();
        jdbc.sql("delete from tentativi_pagamento").update();
        jdbc.sql("delete from spedizioni_da_preparare").update();
    }

    private long conta(String tabella) {
        return jdbc.sql("select count(*) from " + tabella).query(Long.class).single();
    }

    private static RichiestaOrdine richiesta(String prezzo) {
        return new RichiestaOrdine("C-1", List.of(new RichiestaOrdine.Riga("ABC-1", 1, Denaro.euro(prezzo))));
    }
}
