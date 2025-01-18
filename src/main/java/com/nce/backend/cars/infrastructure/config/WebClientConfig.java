package com.nce.backend.cars.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String BASE_URL = "https://akfell-datautlevering.atlas.vegvesen.no/enkeltoppslag/kjoretoydata";

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }
}
