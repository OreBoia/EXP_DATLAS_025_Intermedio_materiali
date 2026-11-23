package com.shopflow.comune;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

// MG1 Base: value object immutabile, validato nel costruttore compatto
public record Denaro(BigDecimal importo, Currency valuta) {

    public static final Currency EUR = Currency.getInstance("EUR");

    public Denaro {
        if (importo == null || valuta == null)
            throw new IllegalArgumentException("importo e valuta sono obbligatori");
        if (importo.signum() < 0)                       // invariante: mai negativo
            throw new IllegalArgumentException("importo negativo");
        // BigDecimal.equals confronta anche la scala: 10.0 e 10.00 sarebbero diversi
        importo = importo.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Denaro euro(String importo) {
        return new Denaro(new BigDecimal(importo), EUR);
    }

    public static Denaro zero(Currency valuta) {
        return new Denaro(BigDecimal.ZERO, valuta);
    }

    // L'oggetto è immutabile: la somma restituisce un NUOVO Denaro
    public Denaro somma(Denaro altro) {
        if (!valuta.equals(altro.valuta()))
            throw new IllegalArgumentException("valute diverse");
        return new Denaro(importo.add(altro.importo()), valuta);
    }

    public Denaro per(int quantita) {
        return new Denaro(importo.multiply(BigDecimal.valueOf(quantita)), valuta);
    }

    public boolean superiore(Denaro altro) {
        if (!valuta.equals(altro.valuta()))
            throw new IllegalArgumentException("valute diverse");
        return importo.compareTo(altro.importo()) > 0;
    }
}
