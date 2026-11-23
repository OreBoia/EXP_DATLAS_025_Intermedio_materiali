package com.shopflow.ordini.dominio;

import com.shopflow.ordini.OrdineId;
import java.util.Optional;

// MG3: PORTA in uscita, con i termini del dominio. L'implementazione JPA sta in ordini.interno
public interface OrdineRepository {
    Optional<Ordine> trova(OrdineId id);
    void salva(Ordine ordine);
}
