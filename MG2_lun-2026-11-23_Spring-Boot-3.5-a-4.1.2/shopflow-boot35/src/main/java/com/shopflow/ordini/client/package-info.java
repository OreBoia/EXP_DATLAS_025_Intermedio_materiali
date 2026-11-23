/**
 * MG2 Avanzato: client dichiarativo verso un servizio di magazzino REMOTO (anticipa MG6, dove magazzino diventa
 * {@code inventory-service}). Nel monolite il magazzino è un modulo locale: questo client non è usato dal caso
 * d'uso CreaOrdine, ma è configurato e testato contro un server HTTP finto.
 * <p>
 * Tutto il package è NON nullo, salvo dove indicato con {@code @Nullable} (annotazioni di Spring).
 */
@NonNullApi
package com.shopflow.ordini.client;

import org.springframework.lang.NonNullApi;
