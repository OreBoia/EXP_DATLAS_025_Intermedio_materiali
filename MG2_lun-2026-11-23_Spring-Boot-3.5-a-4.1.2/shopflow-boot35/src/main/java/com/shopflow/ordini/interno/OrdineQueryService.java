package com.shopflow.ordini.interno;

import com.shopflow.comune.Denaro;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.dominio.OrdineRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Letture per il controller, una forma di risposta per versione dell'API (MG2)
@Service
class OrdineQueryService {

    private final OrdineRepository ordini;

    OrdineQueryService(OrdineRepository ordini) {
        this.ordini = ordini;
    }

    @Transactional(readOnly = true)                      // MG5: solo lettura
    public Optional<OrdineV1> v1(OrdineId id) {
        return ordini.trova(id).map(o -> new OrdineV1(o.id().valore(), o.totale().importo()));
    }

    @Transactional(readOnly = true)
    public Optional<OrdineV2> v2(OrdineId id) {
        return ordini.trova(id).map(o -> new OrdineV2(o.id().valore(), o.totale(), o.creatoIl()));
    }
}

// v1: il totale è un semplice numero
record OrdineV1(UUID id, BigDecimal totale) {}

// v2: il totale diventa un oggetto con importo e valuta
record OrdineV2(UUID id, Denaro totale, Instant creatoIl) {}
