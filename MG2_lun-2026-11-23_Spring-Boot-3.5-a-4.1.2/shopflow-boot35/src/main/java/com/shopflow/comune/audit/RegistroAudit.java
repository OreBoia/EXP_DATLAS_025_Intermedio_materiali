package com.shopflow.comune.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Destinazione dell'audit: scrive nel log una riga JSON che altri strumenti (raccolta log, allarmi) leggono.
// Il formato è un contratto indipendente dall'API REST, per questo il registro ha un mapper TUTTO SUO.
// Nei test il registro si sostituisce con un mock (MG4)
@Component
public class RegistroAudit {

    private static final Logger log = LoggerFactory.getLogger(RegistroAudit.class);

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void scrivi(String azione, String metodo, Object esito) {
        log.info("AUDIT {}", comeJson(Instant.now(), azione, metodo, esito));
    }

    // Una riga JSON per voce, con le chiavi in ordine fisso: quando, azione, metodo, esito
    String comeJson(Instant quando, String azione, String metodo, Object esito) {
        var voce = new LinkedHashMap<String, Object>();
        voce.put("quando", quando);
        voce.put("azione", azione);
        voce.put("metodo", metodo);
        voce.put("esito", esito);
        try {
            return mapper.writeValueAsString(voce);
        } catch (JsonProcessingException e) {                    // eccezione controllata in Jackson 2
            return "{\"errore\":\"voce di audit non serializzabile\"}";
        }
    }
}
