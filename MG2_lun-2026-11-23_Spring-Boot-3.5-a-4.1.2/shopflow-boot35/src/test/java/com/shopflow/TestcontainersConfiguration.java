package com.shopflow;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

// MG5: "meglio un PostgreSQL vero (Testcontainers) di un database in memoria"
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection                                   // Boot ricava url, utente e password dal container
    PostgreSQLContainer<?> postgres() {                  // Testcontainers 1.x: classe generica
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));
    }
}
