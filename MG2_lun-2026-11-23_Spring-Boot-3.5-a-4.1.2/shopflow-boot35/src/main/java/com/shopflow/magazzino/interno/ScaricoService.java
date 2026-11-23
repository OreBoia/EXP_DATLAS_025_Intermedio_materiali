package com.shopflow.magazzino.interno;

import com.shopflow.ordini.spi.ScaricoMagazzino;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// MG5: implementazione della porta di ordini. REQUIRED (default): si unisce alla transazione dell'ordine
@Service
class ScaricoService implements ScaricoMagazzino {

    private final ArticoloJpaRepository articoli;

    ScaricoService(ArticoloJpaRepository articoli) {
        this.articoli = articoli;
    }

    @Override
    @Transactional
    public void scarica(String sku, int quantita) {
        var articolo = articoli.findById(sku)
                .orElseThrow(() -> new IllegalArgumentException("articolo sconosciuto: " + sku));
        articolo.scarica(quantita);               // nessun save(): il dirty checking scrive al commit
    }   // se un'altra transazione ha già modificato la riga: ObjectOptimisticLockingFailureException
}
