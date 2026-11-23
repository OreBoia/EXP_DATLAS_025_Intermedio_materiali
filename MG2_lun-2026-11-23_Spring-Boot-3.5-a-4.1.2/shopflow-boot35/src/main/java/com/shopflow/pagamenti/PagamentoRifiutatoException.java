package com.shopflow.pagamenti;

// MG5 Intermedio: eccezione CONTROLLATA. Per default @Transactional farebbe commit: serve rollbackFor
public class PagamentoRifiutatoException extends Exception {

    public PagamentoRifiutatoException(String motivo) {
        super(motivo);
    }
}
