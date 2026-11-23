package com.shopflow.ordini.interno;

import com.shopflow.ordini.OrdineId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// MG5 Intermedio: transazione INDIPENDENTE, il suo commit non dipende da quello del chiamante
@Service
class RegistroTentativi {

    private final TentativoJpaRepository tentativi;

    RegistroTentativi(TentativoJpaRepository tentativi) {
        this.tentativi = tentativi;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registra(OrdineId ordine, String esito, String dettaglio) {
        tentativi.save(new TentativoEntity(ordine.valore(), esito, dettaglio));
    }
}

@Entity
@Table(name = "tentativi_pagamento")
class TentativoEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID ordineId;
    private String esito;
    private String dettaglio;
    private Instant quando;

    protected TentativoEntity() {}

    TentativoEntity(UUID ordineId, String esito, String dettaglio) {
        this.ordineId = ordineId;
        this.esito = esito;
        this.dettaglio = dettaglio;
        this.quando = Instant.now();
    }
}

interface TentativoJpaRepository extends JpaRepository<TentativoEntity, UUID> {}
