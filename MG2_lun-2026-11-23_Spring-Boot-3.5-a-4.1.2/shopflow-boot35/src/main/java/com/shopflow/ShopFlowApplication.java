package com.shopflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// MG3: classe principale. Ogni sotto-package diretto (comune, ordini, magazzino, pagamenti) è un modulo
@SpringBootApplication
public class ShopFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopFlowApplication.class, args);
    }
}
