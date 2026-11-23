package com.shopflow.ordini.interno;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shopflow.comune.Denaro;
import com.shopflow.ordini.CreaOrdine;
import com.shopflow.ordini.OrdineId;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// MG2 Intermedio: un test per ogni versione pubblicata + versione non supportata
@WebMvcTest(OrdineController.class)
class OrdineControllerVersioniTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000042");
    private static final Instant CREATO = Instant.parse("2026-11-23T14:30:00Z");

    @Autowired MockMvc mockMvc;
    @MockBean CreaOrdine creaOrdine;                      // sostituisce il bean nel contesto con un mock
    @MockBean OrdineQueryService servizio;

    @BeforeEach
    void dati() {
        var id = new OrdineId(ID);
        given(servizio.v1(id)).willReturn(Optional.of(new OrdineV1(ID, Denaro.euro("25.50").importo())));
        given(servizio.v2(id)).willReturn(Optional.of(new OrdineV2(ID, Denaro.euro("25.50"), CREATO)));
    }

    @Test
    void senzaHeaderRispondeLaV1() throws Exception {
        mockMvc.perform(get("/ordini/{id}", ID))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totale").value(25.50));          // v1: un semplice numero
    }

    @Test
    void conHeader2RispondeLaV2() throws Exception {
        mockMvc.perform(get("/ordini/{id}", ID).header("X-API-Version", "2.0"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totale.valuta").value("EUR"))    // forma della v2
               .andExpect(jsonPath("$.totale.importo").value(25.50))
               // Jackson 2 + Boot: le date sono già testo ISO-8601 (WRITE_DATES_AS_TIMESTAMPS è disattivato da Boot)
               .andExpect(jsonPath("$.creatoIl").value("2026-11-23T14:30:00Z"));
    }

    // Senza supporto nativo al versionamento, una versione sconosciuta non è un errore: cade sulla mappatura di default
    @Test
    void versioneSconosciutaRiceveLaV1() throws Exception {
        mockMvc.perform(get("/ordini/{id}", ID).header("X-API-Version", "9.9"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.totale").value(25.50));
    }

    @Test
    void ordineInesistenteDa404() throws Exception {
        mockMvc.perform(get("/ordini/{id}", UUID.randomUUID()))
               .andExpect(status().isNotFound());
    }
}
