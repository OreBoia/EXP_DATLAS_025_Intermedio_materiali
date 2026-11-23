package com.shopflow.ordini.interno;

import com.shopflow.ordini.CreaOrdine;
import com.shopflow.ordini.OrdineId;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// MG3: adattatore in ingresso, traduce HTTP in una chiamata alla porta
// MG2: due versioni dello stesso endpoint, scelte a mano con la condizione "headers" della mappatura:
// con Spring Framework 6.2 non esiste un supporto nativo al versionamento
@RestController
@RequestMapping("/ordini")
class OrdineController {

    private final CreaOrdine creaOrdine;                 // dipende dalla porta
    private final OrdineQueryService servizio;

    OrdineController(CreaOrdine creaOrdine, OrdineQueryService servizio) {
        this.creaOrdine = creaOrdine;
        this.servizio = servizio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrdineId crea(@Valid @RequestBody RichiestaOrdine richiesta) throws PagamentoRifiutatoException {
        return creaOrdine.esegui(richiesta);
    }

    // v1: è la versione di default, risponde anche quando l'header X-API-Version manca o ha un altro valore
    @GetMapping("/{id}")
    ResponseEntity<OrdineV1> dettaglioV1(@PathVariable UUID id) {
        return ResponseEntity.of(servizio.v1(new OrdineId(id)));
    }

    // v2: scelta solo se l'header vale esattamente 2.0 (la mappatura più specifica vince)
    @GetMapping(path = "/{id}", headers = "X-API-Version=2.0")
    ResponseEntity<OrdineV2> dettaglioV2(@PathVariable UUID id) {
        return ResponseEntity.of(servizio.v2(new OrdineId(id)));
    }
}
