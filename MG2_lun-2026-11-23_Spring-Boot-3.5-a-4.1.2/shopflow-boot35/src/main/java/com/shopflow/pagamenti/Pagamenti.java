package com.shopflow.pagamenti;

import com.shopflow.comune.Denaro;

// API del modulo pagamenti (MG5 Intermedio)
public interface Pagamenti {
    /** Addebita l'importo e restituisce l'id del pagamento. */
    String addebita(String riferimento, Denaro importo) throws PagamentoRifiutatoException;
}
