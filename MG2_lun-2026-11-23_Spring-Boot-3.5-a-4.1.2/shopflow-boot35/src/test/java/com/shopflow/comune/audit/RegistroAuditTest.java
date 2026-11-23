package com.shopflow.comune.audit;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopflow.ordini.OrdineId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

// Il formato della riga di audit è un contratto: chi legge i log si aspetta esattamente questa forma
class RegistroAuditTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000042");

    @Test
    void laRigaDiAuditHaIlFormatoAtteso() {
        var riga = new RegistroAudit().comeJson(
                Instant.parse("2026-11-23T14:30:00Z"), "CREA_ORDINE", "esegui", new OrdineId(ID));

        assertThat(riga).isEqualTo(
                "{\"quando\":1795444200.000000000,\"azione\":\"CREA_ORDINE\",\"metodo\":\"esegui\","
                + "\"esito\":{\"valore\":\"00000000-0000-0000-0000-000000000042\"}}");
    }
}
