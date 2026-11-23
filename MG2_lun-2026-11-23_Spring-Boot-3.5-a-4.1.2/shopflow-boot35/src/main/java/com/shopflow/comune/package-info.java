/**
 * Tipi condivisi da tutti i moduli: value object ({@link com.shopflow.comune.Denaro}), {@link com.shopflow.comune.Result},
 * configurazione web e audit. Modulo OPEN: gli altri moduli possono usarne anche i sotto-package.
 */
@ApplicationModule(type = ApplicationModule.Type.OPEN)
package com.shopflow.comune;

import org.springframework.modulith.ApplicationModule;
