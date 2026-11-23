package com.shopflow.ordini.spi;

// MG5: eccezione NON controllata, quindi provoca il rollback senza configurazione
public class StockInsufficienteException extends RuntimeException {

    private final String sku;

    public StockInsufficienteException(String sku) {
        super("stock insufficiente per " + sku);
        this.sku = sku;
    }

    public String sku() {
        return sku;
    }
}
