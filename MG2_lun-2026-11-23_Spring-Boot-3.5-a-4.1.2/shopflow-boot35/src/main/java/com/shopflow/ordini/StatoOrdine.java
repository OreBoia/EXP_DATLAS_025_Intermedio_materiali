package com.shopflow.ordini;

// MG1 Intermedio: insieme CHIUSO di stati, ognuno con i suoi dati
public sealed interface StatoOrdine {
    record Creato() implements StatoOrdine {}
    record Pagato(String idPagamento) implements StatoOrdine {}
    record Spedito(String corriere, String tracking) implements StatoOrdine {}
    record Annullato(String motivo) implements StatoOrdine {}

    // Nessun default: se aggiungo uno stato, questo switch NON compila più
    static String descrizione(StatoOrdine stato) {
        return switch (stato) {
            case Creato _ -> "In attesa di pagamento";
            case Pagato(String idPagamento) -> "Pagato, rif. " + idPagamento;
            case Spedito(String corriere, _) -> "Spedito con " + corriere;
            case Annullato(var motivo) -> "Annullato: " + motivo;
        };
    }
}
