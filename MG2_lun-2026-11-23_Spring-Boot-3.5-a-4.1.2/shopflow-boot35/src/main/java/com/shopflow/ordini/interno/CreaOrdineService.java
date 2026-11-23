package com.shopflow.ordini.interno;

import com.shopflow.comune.audit.Audited;
import com.shopflow.ordini.CreaOrdine;
import com.shopflow.ordini.OrdineConfermato;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.ordini.dominio.Ordine;
import com.shopflow.ordini.dominio.OrdineRepository;
import com.shopflow.ordini.spi.ScaricoMagazzino;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import com.shopflow.pagamenti.Pagamenti;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// MG3 + MG4 + MG5: il caso d'uso "crea ordine"
@Service
class CreaOrdineService implements CreaOrdine {

    private final OrdineRepository ordini;               // porta del dominio, non JPA
    private final ScaricoMagazzino magazzino;            // porta in uscita, implementata da magazzino
    private final Pagamenti pagamenti;
    private final RegistroTentativi registro;
    private final ApplicationEventPublisher eventi;

    CreaOrdineService(OrdineRepository ordini, ScaricoMagazzino magazzino, Pagamenti pagamenti,
                      RegistroTentativi registro, ApplicationEventPublisher eventi) {
        this.ordini = ordini;
        this.magazzino = magazzino;
        this.pagamenti = pagamenti;
        this.registro = registro;
        this.eventi = eventi;
    }

    @Audited(azione = "CREA_ORDINE")                     // MG4: basta questa riga sul metodo
    @Transactional(rollbackFor = Exception.class)        // MG5: anche le eccezioni controllate fanno rollback
    @Override
    public OrdineId esegui(RichiestaOrdine richiesta) throws PagamentoRifiutatoException {
        var ordine = Ordine.crea(richiesta);             // regole nel dominio
        ordini.salva(ordine);                            // scrittura 1: l'ordine
        ordine.righe().forEach(r ->
                magazzino.scarica(r.sku(), r.quantita()));   // scrittura 2: se lancia, rollback di TUTTO
        try {
            var idPagamento = pagamenti.addebita(ordine.id().toString(), ordine.totale());
            ordine.pagato(idPagamento);
        } catch (PagamentoRifiutatoException e) {
            registro.registra(ordine.id(), "RIFIUTATO", e.getMessage());   // REQUIRES_NEW: resta salvato
            throw e;                                     // rilancia: l'ordine viene annullato
        }
        ordini.salva(ordine);
        // MG4: chi ascolta non è noto qui. Con il registro di Modulith (MG5) l'evento è salvato nella stessa transazione
        eventi.publishEvent(new OrdineConfermato(ordine.id(), Instant.now()));
        return ordine.id();
    }

    // SOLO DIDATTICO (MG4): self-invocation, la chiamata su this salta il proxy (niente audit, niente transazione)
    public OrdineId eseguiDaDentro(RichiestaOrdine richiesta) throws PagamentoRifiutatoException {
        return this.esegui(richiesta);
    }
}
