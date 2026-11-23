package com.shopflow.pagamenti.interno;

import com.shopflow.comune.Denaro;
import com.shopflow.pagamenti.PagamentoRifiutatoException;
import com.shopflow.pagamenti.Pagamenti;
import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Gestore simulato: rifiuta gli importi oltre una soglia, così si può provare il rollback (MG5)
@Service
class GestorePagamentiSimulato implements Pagamenti {

    private static final Logger log = LoggerFactory.getLogger(GestorePagamentiSimulato.class);

    private final BigDecimal sogliaMassima;

    GestorePagamentiSimulato(@Value("${shopflow.pagamenti.soglia-massima:1000.00}") BigDecimal sogliaMassima) {
        this.sogliaMassima = sogliaMassima;
    }

    @Override
    public String addebita(String riferimento, Denaro importo) throws PagamentoRifiutatoException {
        if (importo.importo().compareTo(sogliaMassima) > 0)
            throw new PagamentoRifiutatoException("importo " + importo.importo() + " oltre la soglia " + sogliaMassima);
        var idPagamento = "PAG-" + UUID.randomUUID();
        log.info("addebito {} {} per {}: {}", importo.importo(), importo.valuta(), riferimento, idPagamento);
        return idPagamento;
    }
}
