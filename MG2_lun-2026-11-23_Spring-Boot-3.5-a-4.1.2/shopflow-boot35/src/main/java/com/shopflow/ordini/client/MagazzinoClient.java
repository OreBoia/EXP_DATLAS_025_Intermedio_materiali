package com.shopflow.ordini.client;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

// MG2 Avanzato: l'interfaccia dichiara le chiamate, Spring genera l'implementazione a runtime.
// Null-safety con le annotazioni di Spring (org.springframework.lang): @Nullable segnala il parametro opzionale
@HttpExchange("/giacenze")
public interface MagazzinoClient {

    @GetExchange("/{sku}")
    Giacenza giacenza(@PathVariable String sku,
                      @RequestParam @Nullable String deposito);   // parametro opzionale

    @PostExchange("/riserve")
    Riserva riserva(@RequestBody RichiestaRiserva richiesta);

    record Giacenza(String sku, int quantita) {}

    record RichiestaRiserva(String sku, int quantita) {}

    record Riserva(String id, String sku, int quantita) {}
}
