package com.shopflow.ordini.interno;

import com.shopflow.ordini.spi.StockInsufficienteException;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Traduce le eccezioni del caso d'uso in risposte application/problem+json
@RestControllerAdvice(assignableTypes = OrdineController.class)
class ErroriOrdineHttp {

    @ExceptionHandler
    ProblemDetail stock(StockInsufficienteException e) {
        var p = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        p.setTitle("Stock insufficiente");
        p.setProperty("sku", e.sku());
        return p;
    }

    @ExceptionHandler
    ProblemDetail pagamento(PagamentoRifiutatoException e) {
        var p = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        p.setTitle("Pagamento rifiutato");
        return p;
    }

    @ExceptionHandler
    ProblemDetail richiestaNonValida(IllegalArgumentException e) {
        var p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        p.setTitle("Richiesta non valida");
        return p;
    }
}
