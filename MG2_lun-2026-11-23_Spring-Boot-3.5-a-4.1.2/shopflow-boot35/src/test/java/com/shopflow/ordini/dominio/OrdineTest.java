package com.shopflow.ordini.dominio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shopflow.comune.Denaro;
import com.shopflow.comune.Result;
import com.shopflow.ordini.ErroreOrdine;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.ordini.StatoOrdine;
import java.util.List;
import org.junit.jupiter.api.Test;

// MG3: le regole di business si testano senza avviare Spring, in millisecondi
class OrdineTest {

    private static RichiestaOrdine richiesta(int quantita) {
        return new RichiestaOrdine("C-1", List.of(
                new RichiestaOrdine.Riga("ABC-1", quantita, Denaro.euro("10.00")),
                new RichiestaOrdine.Riga("ABC-2", 1, Denaro.euro("5.50"))));
    }

    @Test
    void creaCalcolaIlTotaleEPartedaCreato() {
        var ordine = Ordine.crea(richiesta(2));
        assertThat(ordine.totale()).isEqualTo(Denaro.euro("25.50"));
        assertThat(ordine.stato()).isEqualTo(new StatoOrdine.Creato());
        assertThat(ordine.righe()).hasSize(2);
    }

    @Test
    void laRadiceRifiutaQuantitaNonPositive() {
        assertThatThrownBy(() -> Ordine.crea(richiesta(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("la quantità deve essere positiva");
    }

    @Test
    void leRigheNonSiModificanoDaFuori() {
        var ordine = Ordine.crea(richiesta(1));
        assertThatThrownBy(() -> ordine.righe().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void transizioniDiStato() {
        var ordine = Ordine.crea(richiesta(1));
        ordine.pagato("PAG-1");
        assertThat(StatoOrdine.descrizione(ordine.stato())).isEqualTo("Pagato, rif. PAG-1");
        assertThatThrownBy(() -> ordine.pagato("PAG-2")).isInstanceOf(IllegalStateException.class);
        ordine.spedito("BRT", "TRK-1");
        assertThat(StatoOrdine.descrizione(ordine.stato())).isEqualTo("Spedito con BRT");
        assertThatThrownBy(() -> ordine.annulla("ripensamento")).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void messaggiDegliErrori() {
        Result<String, ErroreOrdine> esaurito = new Result.Errore<>(new ErroreOrdine.StockInsufficiente("ABC-3", 2, 0));
        Result<String, ErroreOrdine> pochi = new Result.Errore<>(new ErroreOrdine.StockInsufficiente("ABC-2", 5, 3));
        assertThat(ErroreOrdine.messaggio(esaurito)).isEqualTo("Articolo esaurito: ABC-3");         // guardia when
        assertThat(ErroreOrdine.messaggio(pochi)).isEqualTo("Di ABC-2 restano 3 pezzi su 5");
    }
}
