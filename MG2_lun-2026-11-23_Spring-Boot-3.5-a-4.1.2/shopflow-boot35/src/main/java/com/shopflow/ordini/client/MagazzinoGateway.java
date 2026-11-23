package com.shopflow.ordini.client;

import com.shopflow.comune.Result;
import com.shopflow.ordini.ErroreOrdine;
import com.shopflow.ordini.client.MagazzinoClient.Giacenza;
import com.shopflow.ordini.client.MagazzinoClient.RichiestaRiserva;
import com.shopflow.ordini.client.MagazzinoClient.Riserva;
import java.util.concurrent.Semaphore;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

// MG4: resilienza con Spring Retry (richiede @EnableRetry, vedi comune.ResilienzaConfig)
// MG2 Avanzato: adattatore in uscita che traduce le eccezioni HTTP in errori di dominio
@Service
public class MagazzinoGateway {

    private final MagazzinoClient client;
    // Spring Framework 6.2 non ha un limite di concorrenza dichiarativo: si usa un semaforo a mano
    private final Semaphore concorrenza = new Semaphore(10);

    MagazzinoGateway(MagazzinoClient client) {
        this.client = client;
    }

    @Retryable(
        retryFor = ResourceAccessException.class,    // riprova solo per timeout e problemi di rete
        maxAttempts = 4,                             // 4 chiamate in tutto: il primo tentativo + 3 ripetizioni
        backoff = @Backoff(delay = 200, multiplier = 2, maxDelay = 2000, random = true))   // attese crescenti con variazione casuale
    public Giacenza giacenza(String sku) {
        return client.giacenza(sku, null);
    }

    public Riserva riserva(RichiestaRiserva richiesta) {
        concorrenza.acquireUninterruptibly();        // al massimo 10 chiamate contemporanee
        try {
            return client.riserva(richiesta);
        } finally {
            concorrenza.release();
        }
    }

    // Gli errori tornano come Result (MG1): i dettagli HTTP non arrivano al servizio di dominio
    public Result<Riserva, ErroreOrdine> riservaSicura(RichiestaRiserva richiesta) {
        try {
            return new Result.Ok<>(client.riserva(richiesta));
        } catch (HttpClientErrorException.NotFound e) {                      // 404: articolo sconosciuto
            return new Result.Errore<>(new ErroreOrdine.ArticoloSconosciuto(richiesta.sku()));
        } catch (HttpServerErrorException | ResourceAccessException e) {    // 5xx oppure timeout/rete
            return new Result.Errore<>(new ErroreOrdine.MagazzinoNonDisponibile());
        }
    }
}
