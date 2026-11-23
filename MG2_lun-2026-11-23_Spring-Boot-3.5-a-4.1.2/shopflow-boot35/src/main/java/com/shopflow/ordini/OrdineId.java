package com.shopflow.ordini;

import java.util.UUID;

// MG3: tipo che compare nelle firme dell'API, quindi sta nel package radice del modulo.
// L'id è creato dal dominio (OrdineId.nuovo()), non dal database.
public record OrdineId(UUID valore) {

    public OrdineId {
        if (valore == null) throw new IllegalArgumentException("id obbligatorio");
    }

    public static OrdineId nuovo() {
        return new OrdineId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return valore.toString();
    }
}
