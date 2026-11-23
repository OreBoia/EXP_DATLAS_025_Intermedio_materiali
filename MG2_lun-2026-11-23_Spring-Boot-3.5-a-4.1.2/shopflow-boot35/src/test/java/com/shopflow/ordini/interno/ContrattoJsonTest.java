package com.shopflow.ordini.interno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.shopflow.comune.Denaro;
import com.shopflow.comune.web.JsonConfig;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;

// Test di regressione del JSON dell'API: fotografa il contratto prodotto dal mapper di Boot (Jackson 2).
// @JsonTest carica solo la parte JSON del contesto (ObjectMapper, @JsonComponent), senza database
@JsonTest
@Import(JsonConfig.class)
class ContrattoJsonTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000042");

    @Autowired ObjectMapper mapper;

    @Test
    void ordineV2HaLaFormaDelContratto() throws Exception {
        var v2 = new OrdineV2(ID, Denaro.euro("25.50"), Instant.parse("2026-11-23T14:30:00Z"));

        // Confronto sulla stringa intera: con Jackson 2 le proprietà escono nell'ordine di dichiarazione del record
        assertThat(mapper.writeValueAsString(v2)).isEqualTo(
                "{\"id\":\"00000000-0000-0000-0000-000000000042\","
                + "\"totale\":{\"importo\":25.50,\"valuta\":\"EUR\"},"
                + "\"creatoIl\":\"2026-11-23T14:30:00Z\"}");
    }

    @Test
    void ordineV1HaIlTotaleComeNumero() throws Exception {
        var v1 = new OrdineV1(ID, Denaro.euro("25.50").importo());
        assertThat(mapper.writeValueAsString(v1))
                .isEqualTo("{\"id\":\"00000000-0000-0000-0000-000000000042\",\"totale\":25.50}");
    }

    @Test
    void denaroSiLeggeDalFormatoDelContratto() throws Exception {
        var denaro = mapper.readValue("{\"importo\":\"10.5\",\"valuta\":\"EUR\"}", Denaro.class);
        assertThat(denaro).isEqualTo(Denaro.euro("10.50"));
    }

    @Test
    void denaroSenzaImportoEUnErroreDiLettura() {
        assertThatThrownBy(() -> mapper.readValue("{\"valuta\":\"EUR\"}", Denaro.class))
                .isInstanceOf(MismatchedInputException.class)
                .hasMessageContaining("importo e valuta sono obbligatori");
    }
}
