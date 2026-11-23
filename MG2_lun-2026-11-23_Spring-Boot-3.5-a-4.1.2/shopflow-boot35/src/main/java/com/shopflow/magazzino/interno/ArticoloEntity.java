package com.shopflow.magazzino.interno;

import com.shopflow.ordini.spi.StockInsufficienteException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

// MG5 Avanzato: lock ottimistico con @Version
@Entity
@Table(name = "articoli")
class ArticoloEntity {

    @Id
    private String sku;
    private String descrizione;
    private int giacenza;

    @Version
    private long versione;                       // Hibernate: UPDATE ... WHERE sku = ? AND versione = ?

    protected ArticoloEntity() {}

    ArticoloEntity(String sku, String descrizione, int giacenza) {
        this.sku = sku;
        this.descrizione = descrizione;
        this.giacenza = giacenza;
    }

    void scarica(int quantita) {
        if (giacenza < quantita) throw new StockInsufficienteException(sku);
        giacenza -= quantita;
    }

    String sku() { return sku; }
    int giacenza() { return giacenza; }
}
