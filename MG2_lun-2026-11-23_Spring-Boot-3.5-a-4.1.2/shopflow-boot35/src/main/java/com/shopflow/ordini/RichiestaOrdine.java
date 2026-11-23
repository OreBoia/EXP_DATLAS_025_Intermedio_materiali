package com.shopflow.ordini;

import com.shopflow.comune.Denaro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

// MG3: dati in ingresso del caso d'uso CreaOrdine
public record RichiestaOrdine(@NotBlank String idCliente, @NotEmpty List<@Valid Riga> righe) {

    public record Riga(@NotBlank String sku, @Positive int quantita, @NotNull Denaro prezzo) {}

    public RichiestaOrdine {
        righe = righe == null ? List.of() : List.copyOf(righe);
    }
}
