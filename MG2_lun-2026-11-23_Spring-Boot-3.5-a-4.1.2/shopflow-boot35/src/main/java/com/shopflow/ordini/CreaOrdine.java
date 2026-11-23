package com.shopflow.ordini;

import com.shopflow.pagamenti.PagamentoRifiutatoException;

// MG3: PORTA in ingresso, il caso d'uso che il mondo esterno può chiamare.
// MG5 Intermedio: il pagamento può essere rifiutato (eccezione controllata).
public interface CreaOrdine {
    OrdineId esegui(RichiestaOrdine richiesta) throws PagamentoRifiutatoException;
}
