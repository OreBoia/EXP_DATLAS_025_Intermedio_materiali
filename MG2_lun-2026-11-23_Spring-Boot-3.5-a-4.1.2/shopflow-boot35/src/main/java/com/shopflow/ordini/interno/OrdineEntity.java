package com.shopflow.ordini.interno;

import com.shopflow.comune.Denaro;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.StatoOrdine;
import com.shopflow.ordini.dominio.Ordine;
import com.shopflow.ordini.dominio.RigaOrdine;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

// MG3: entità JPA SEPARATA dal dominio (Ordine). Il database non detta il modello
@Entity
@Table(name = "ordini")
class OrdineEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String idCliente;

    @Column(nullable = false)
    private Instant creatoIl;

    @Column(nullable = false, length = 3)
    private String valuta;

    @Column(nullable = false, length = 20)
    private String stato;

    private String idPagamento;
    private String corriere;
    private String tracking;
    private String motivo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "righe_ordine", joinColumns = @JoinColumn(name = "ordine_id"))
    @OrderColumn(name = "posizione")
    private List<Riga> righe = new ArrayList<>();

    protected OrdineEntity() {}                      // richiesto da JPA

    static OrdineEntity da(Ordine ordine) {
        var e = new OrdineEntity();
        e.id = ordine.id().valore();
        e.idCliente = ordine.idCliente();
        e.creatoIl = ordine.creatoIl();
        e.valuta = ordine.totale().valuta().getCurrencyCode();
        ordine.righe().forEach(r -> e.righe.add(new Riga(r.sku(), r.quantita(), r.prezzo().importo())));
        e.aggiornaDa(ordine);
        return e;
    }

    // Le righe non cambiano dopo la creazione: si aggiorna solo lo stato
    void aggiornaDa(Ordine ordine) {
        idPagamento = null;
        corriere = null;
        tracking = null;
        motivo = null;
        stato = switch (ordine.stato()) {               // MG1: switch esaustivo sulla gerarchia sealed
            case StatoOrdine.Creato _ -> "CREATO";
            case StatoOrdine.Pagato(var idPag) -> { idPagamento = idPag; yield "PAGATO"; }
            case StatoOrdine.Spedito(var c, var t) -> { corriere = c; tracking = t; yield "SPEDITO"; }
            case StatoOrdine.Annullato(var m) -> { motivo = m; yield "ANNULLATO"; }
        };
    }

    Ordine inDominio() {
        var currency = Currency.getInstance(valuta);
        StatoOrdine s = switch (stato) {
            case "CREATO" -> new StatoOrdine.Creato();
            case "PAGATO" -> new StatoOrdine.Pagato(idPagamento);
            case "SPEDITO" -> new StatoOrdine.Spedito(corriere, tracking);
            case "ANNULLATO" -> new StatoOrdine.Annullato(motivo);
            default -> throw new IllegalStateException("stato sconosciuto nel database: " + stato);
        };
        var righeDominio = righe.stream()
                .map(r -> new RigaOrdine(r.sku, r.quantita, new Denaro(r.prezzo, currency)))
                .toList();
        return Ordine.ricostruisci(new OrdineId(id), idCliente, creatoIl, s, righeDominio);
    }

    UUID id() {
        return id;
    }

    @Embeddable
    static class Riga {
        @Column(nullable = false)
        String sku;
        int quantita;
        @Column(nullable = false, precision = 19, scale = 2)
        BigDecimal prezzo;

        protected Riga() {}

        Riga(String sku, int quantita, BigDecimal prezzo) {
            this.sku = sku;
            this.quantita = quantita;
            this.prezzo = prezzo;
        }
    }
}
