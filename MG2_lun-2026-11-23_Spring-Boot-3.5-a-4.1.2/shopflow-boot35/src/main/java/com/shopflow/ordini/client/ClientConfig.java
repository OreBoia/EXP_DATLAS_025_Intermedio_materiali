package com.shopflow.ordini.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

// Con Spring Framework 6.2 il client @HttpExchange si costruisce a mano: RestClient -> adattatore -> proxy.
// Indirizzo e timeout arrivano da MagazzinoProperties (shopflow.magazzino.*)
@Configuration
@EnableConfigurationProperties(MagazzinoProperties.class)
class ClientConfig {

    @Bean
    MagazzinoClient magazzinoClient(RestClient.Builder builder, MagazzinoProperties proprieta) {
        var impostazioni = ClientHttpRequestFactorySettings.defaults().withReadTimeout(proprieta.readTimeout());
        var restClient = builder
                .baseUrl(proprieta.baseUrl())
                .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(impostazioni))
                .build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(MagazzinoClient.class);
    }
}
