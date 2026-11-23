/**
 * Modulo magazzino: giacenze, scarico (implementa {@code ordini.spi.ScaricoMagazzino}) e spedizioni da preparare.
 * <p>
 * MG3 Avanzato dichiara solo {@code "ordini::spi"}: qui serve anche {@code "ordini"} (package radice), perché
 * magazzino ascolta l'evento {@code OrdineConfermato} (MG4/MG5), che fa parte dell'API radice di ordini.
 */
@ApplicationModule(allowedDependencies = {"ordini", "ordini::spi", "comune"})
package com.shopflow.magazzino;

import org.springframework.modulith.ApplicationModule;
