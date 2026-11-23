package com.shopflow.ordini.dominio;

import com.shopflow.comune.Denaro;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.ordini.StatoOrdine;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// MG3 Intermedio: radice dell'aggregato Ordine. Nessun import di Spring o JPA (regola ArchUnit)
public class Ordine {

    private final OrdineId id;
    private final String idCliente;
    private final Instant creatoIl;
    private final List<RigaOrdine> righe = new ArrayList<>();   // oggetti interni all'aggregato
    private StatoOrdine stato;

    private Ordine(OrdineId id, String idCliente, Instant creatoIl, StatoOrdine stato) {
        this.id = id;
        this.idCliente = idCliente;
        this.creatoIl = creatoIl;
        this.stato = stato;
    }

    // Unico modo di creare un ordine: le regole passano sempre da aggiungiRiga
    public static Ordine crea(RichiestaOrdine richiesta) {
        if (richiesta.righe().isEmpty())
            throw new IllegalArgumentException("un ordine deve avere almeno una riga");
        var ordine = new Ordine(OrdineId.nuovo(), richiesta.idCliente(), Instant.now(), new StatoOrdine.Creato());
        richiesta.righe().forEach(r -> ordine.aggiungiRiga(r.sku(), r.quantita(), r.prezzo()));
        return ordine;
    }

    // Usato dall'adattatore di persistenza per ricostruire l'aggregato letto dal database
    public static Ordine ricostruisci(OrdineId id, String idCliente, Instant creatoIl,
                                      StatoOrdine stato, List<RigaOrdine> righe) {
        var ordine = new Ordine(id, idCliente, creatoIl, stato);
        ordine.righe.addAll(righe);
        return ordine;
    }

    public void aggiungiRiga(String sku, int quantita, Denaro prezzo) {
        if (!(stato instanceof StatoOrdine.Creato))
            throw new IllegalStateException("le righe si modificano solo su un ordine appena creato");
        if (quantita <= 0)                                      // invariante protetta dalla radice
            throw new IllegalArgumentException("la quantità deve essere positiva");
        if (!righe.isEmpty() && !righe.getFirst().prezzo().valuta().equals(prezzo.valuta()))
            throw new IllegalArgumentException("tutte le righe devono avere la stessa valuta");
        righe.add(new RigaOrdine(sku, quantita, prezzo));
    }

    // Transizioni di stato: lo switch esaustivo dice quali partenze sono ammesse
    public void pagato(String idPagamento) {
        stato = switch (stato) {
            case StatoOrdine.Creato _ -> new StatoOrdine.Pagato(idPagamento);
            case StatoOrdine.Pagato _, StatoOrdine.Spedito _, StatoOrdine.Annullato _ ->
                    throw new IllegalStateException("ordine non pagabile nello stato " + stato);
        };
    }

    public void spedito(String corriere, String tracking) {
        stato = switch (stato) {
            case StatoOrdine.Pagato _ -> new StatoOrdine.Spedito(corriere, tracking);
            case StatoOrdine.Creato _, StatoOrdine.Spedito _, StatoOrdine.Annullato _ ->
                    throw new IllegalStateException("ordine non spedibile nello stato " + stato);
        };
    }

    public void annulla(String motivo) {
        stato = switch (stato) {
            case StatoOrdine.Creato _, StatoOrdine.Pagato _ -> new StatoOrdine.Annullato(motivo);
            case StatoOrdine.Spedito _, StatoOrdine.Annullato _ ->
                    throw new IllegalStateException("ordine non annullabile nello stato " + stato);
        };
    }

    public Denaro totale() {
        return righe.stream()
                .map(RigaOrdine::importo)
                .reduce(Denaro::somma)
                .orElseThrow();                                 // un ordine ha sempre almeno una riga
    }

    public List<RigaOrdine> righe() { return List.copyOf(righe); }   // copia non modificabile
    public OrdineId id() { return id; }
    public String idCliente() { return idCliente; }
    public Instant creatoIl() { return creatoIl; }
    public StatoOrdine stato() { return stato; }
}
