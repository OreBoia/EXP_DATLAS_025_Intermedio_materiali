package com.shopflow.magazzino.interno;

import com.shopflow.ordini.OrdineConfermato;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

// MG4 Avanzato / MG5: magazzino reagisce a OrdineConfermato DOPO il commit di ordini.
// Lo stock è già stato scaricato in CreaOrdineService (MG5 Base): qui si registra la spedizione da preparare.
@Component
class PreparaSpedizione {

    private static final Logger log = LoggerFactory.getLogger(PreparaSpedizione.class);

    private final SpedizioneJpaRepository spedizioni;

    PreparaSpedizione(SpedizioneJpaRepository spedizioni) {
        this.spedizioni = spedizioni;
    }

    @TransactionalEventListener                                  // parte solo dopo il commit
    @Transactional(propagation = Propagation.REQUIRES_NEW)       // per scrivere serve una transazione nuova
    void su(OrdineConfermato evento) {
        var ordineId = evento.id().valore();
        // Consegna "almeno una volta" con il registro eventi (MG5): il listener deve essere idempotente
        if (spedizioni.existsById(ordineId)) {
            log.info("spedizione per l'ordine {} già registrata: evento ignorato", ordineId);
            return;
        }
        spedizioni.save(new SpedizioneEntity(ordineId, evento.quando()));
    }
}

@Entity
@Table(name = "spedizioni_da_preparare")
class SpedizioneEntity {

    @Id
    private UUID ordineId;
    private Instant confermatoIl;

    protected SpedizioneEntity() {}

    SpedizioneEntity(UUID ordineId, Instant confermatoIl) {
        this.ordineId = ordineId;
        this.confermatoIl = confermatoIl;
    }
}

interface SpedizioneJpaRepository extends JpaRepository<SpedizioneEntity, UUID> {}
