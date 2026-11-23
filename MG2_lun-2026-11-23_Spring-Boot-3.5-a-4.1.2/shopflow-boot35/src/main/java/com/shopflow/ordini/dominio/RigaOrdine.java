package com.shopflow.ordini.dominio;

import com.shopflow.comune.Denaro;

// Oggetto interno all'aggregato Ordine: si crea solo passando dalla radice
public record RigaOrdine(String sku, int quantita, Denaro prezzo) {

    public Denaro importo() {
        return prezzo.per(quantita);
    }
}
