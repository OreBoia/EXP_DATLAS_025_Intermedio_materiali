package com.shopflow.ordini.client;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

// Indirizzo e timeout del servizio di magazzino remoto: shopflow.magazzino.* in application.properties
@ConfigurationProperties("shopflow.magazzino")
record MagazzinoProperties(String baseUrl, Duration readTimeout) {}
