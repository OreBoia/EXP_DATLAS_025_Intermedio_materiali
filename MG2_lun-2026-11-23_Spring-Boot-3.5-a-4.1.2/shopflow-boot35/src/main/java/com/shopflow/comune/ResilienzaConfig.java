package com.shopflow.comune;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

// MG4: attiva @Retryable della libreria Spring Retry (in Spring Boot 3.5 la resilienza dichiarativa è questa)
@Configuration
@EnableRetry
class ResilienzaConfig {}
