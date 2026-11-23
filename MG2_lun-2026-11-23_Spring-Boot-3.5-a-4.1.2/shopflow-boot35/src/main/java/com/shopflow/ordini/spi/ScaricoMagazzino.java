package com.shopflow.ordini.spi;

// MG5 Base: porta in uscita di ordini, implementata dal modulo magazzino
public interface ScaricoMagazzino {
    /** Scala la giacenza. Lancia {@link StockInsufficienteException} (RuntimeException) se non basta. */
    void scarica(String sku, int quantita);
}
