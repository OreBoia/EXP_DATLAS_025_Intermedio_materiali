package com.shopflow.ordini;

import com.shopflow.comune.Result;

// MG1 Avanzato + MG2 Avanzato: errori di dominio previsti, come insieme chiuso
public sealed interface ErroreOrdine {
    record StockInsufficiente(String sku, int richiesti, int disponibili) implements ErroreOrdine {}
    record ClienteBloccato(String idCliente) implements ErroreOrdine {}
    record ArticoloSconosciuto(String sku) implements ErroreOrdine {}
    record MagazzinoNonDisponibile() implements ErroreOrdine {}

    // MG1 Avanzato: record pattern annidati e guardia when, senza default
    static <T> String messaggio(Result<T, ErroreOrdine> esito) {
        return switch (esito) {
            case Result.Ok(var valore) -> "Operazione riuscita: " + valore;
            case Result.Errore(StockInsufficiente(var sku, _, var disp)) when disp == 0 -> "Articolo esaurito: " + sku;
            case Result.Errore(StockInsufficiente(var sku, var richiesti, var disp)) ->
                    "Di " + sku + " restano " + disp + " pezzi su " + richiesti;
            case Result.Errore(ClienteBloccato(var id)) -> "Cliente " + id + " bloccato";
            case Result.Errore(ArticoloSconosciuto(var sku)) -> "Articolo sconosciuto: " + sku;
            case Result.Errore(MagazzinoNonDisponibile _) -> "Magazzino non disponibile, riprovare più tardi";
        };
    }
}
