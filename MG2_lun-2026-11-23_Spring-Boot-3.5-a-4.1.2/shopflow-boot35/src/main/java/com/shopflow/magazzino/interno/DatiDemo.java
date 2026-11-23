package com.shopflow.magazzino.interno;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Component;

// Articoli di prova per l'aula. Disattivabile con shopflow.dati-demo=false (lo fanno i test)
@Component
@ConditionalOnBooleanProperty(name = "shopflow.dati-demo", matchIfMissing = true)
class DatiDemo implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatiDemo.class);

    private final ArticoloJpaRepository articoli;

    DatiDemo(ArticoloJpaRepository articoli) {
        this.articoli = articoli;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (articoli.count() > 0) return;
        articoli.saveAll(List.of(
                new ArticoloEntity("ABC-1", "Tazza ShopFlow", 100),
                new ArticoloEntity("ABC-2", "Maglietta ShopFlow", 20),
                new ArticoloEntity("ABC-3", "Zaino ShopFlow", 0)));
        log.info("caricati gli articoli di prova: ABC-1 (100), ABC-2 (20), ABC-3 (esaurito)");
    }
}
