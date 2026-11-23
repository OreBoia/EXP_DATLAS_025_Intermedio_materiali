package com.shopflow.ordini;

import java.time.Instant;

// MG3/MG4: evento pubblicato dal modulo ordini. Fa parte della sua API: chi lo ascolta dipende da ordini, non il contrario
public record OrdineConfermato(OrdineId id, Instant quando) {}
