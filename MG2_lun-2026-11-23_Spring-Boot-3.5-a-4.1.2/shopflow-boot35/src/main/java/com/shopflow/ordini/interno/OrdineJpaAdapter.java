package com.shopflow.ordini.interno;

import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.dominio.Ordine;
import com.shopflow.ordini.dominio.OrdineRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// MG3: adattatore in uscita, implementa la porta del dominio con Spring Data JPA
@Repository
class OrdineJpaAdapter implements OrdineRepository {

    private final OrdineJpaRepository jpa;

    OrdineJpaAdapter(OrdineJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Ordine> trova(OrdineId id) {
        return jpa.findById(id.valore()).map(OrdineEntity::inDominio);
    }

    // Rispetto alla slide (jpa.save(OrdineEntity.da(ordine))) qui si distingue il primo salvataggio
    // dall'aggiornamento: così un secondo salva() aggiorna lo stato senza riscrivere le righe.
    @Override
    @Transactional                                       // REQUIRED: si unisce alla transazione del caso d'uso
    public void salva(Ordine ordine) {
        jpa.findById(ordine.id().valore()).ifPresentOrElse(
                esistente -> esistente.aggiornaDa(ordine),       // dirty checking: UPDATE al commit
                () -> jpa.save(OrdineEntity.da(ordine)));
    }
}
