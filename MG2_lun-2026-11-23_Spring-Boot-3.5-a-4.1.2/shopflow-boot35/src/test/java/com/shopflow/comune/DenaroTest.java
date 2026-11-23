package com.shopflow.comune;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

// MG1 Base: le righe "Uso" della slide, come test
class DenaroTest {

    private final Currency eur = Currency.getInstance("EUR");

    @Test
    void sommaRestituisceUnNuovoDenaro() {
        var a = new Denaro(new BigDecimal("10.00"), eur);
        var b = new Denaro(new BigDecimal("5.50"), eur);
        assertThat(a.somma(b)).hasToString("Denaro[importo=15.50, valuta=EUR]");
    }

    @Test
    void equalsConfrontaIValoriAncheConScalaDiversa() {
        var a = new Denaro(new BigDecimal("10.00"), eur);
        assertThat(a).isEqualTo(new Denaro(new BigDecimal("10.00"), eur));
        assertThat(new Denaro(new BigDecimal("10.0"), eur)).isEqualTo(a);   // stessa scala dopo setScale
    }

    @Test
    void rifiutaImportiNegativiEValuteDiverse() {
        assertThatThrownBy(() -> Denaro.euro("-1")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Denaro.euro("1").somma(new Denaro(BigDecimal.ONE, Currency.getInstance("USD"))))
                .hasMessage("valute diverse");
    }
}
