package com.shopflow;

import org.springframework.boot.SpringApplication;

// Avvia ShopFlow con PostgreSQL in Testcontainers, senza compose.yaml: ./mvnw spring-boot:test-run
public class TestShopFlowApplication {

    public static void main(String[] args) {
        SpringApplication.from(ShopFlowApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
