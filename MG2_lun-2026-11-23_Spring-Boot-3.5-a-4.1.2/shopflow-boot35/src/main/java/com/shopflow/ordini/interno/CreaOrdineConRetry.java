package com.shopflow.ordini.interno;

import com.shopflow.ordini.CreaOrdine;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

// MG5 Avanzato: il retry sta al confine PIÙ ESTERNO del caso d'uso, fuori dalla transazione.
// È l'implementazione di CreaOrdine usata dal controller (@Primary).
@Primary
@Service
class CreaOrdineConRetry implements CreaOrdine {

    private final CreaOrdineService servizio;            // contiene @Transactional

    CreaOrdineConRetry(CreaOrdineService servizio) {
        this.servizio = servizio;
    }

    // Ogni tentativo: transazione nuova, ordine rifatto da capo (e nuovo addebito: con un gestore
    // di pagamento reale l'addebito andrebbe fatto fuori dalla transazione, vedi MG5 e MG9)
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class,
               maxAttempts = 4,                          // 4 tentativi in tutto: il primo + 3 ripetizioni
               backoff = @Backoff(delay = 50))
    @Override
    public OrdineId esegui(RichiestaOrdine richiesta) throws PagamentoRifiutatoException {
        return servizio.esegui(richiesta);
    }
}
