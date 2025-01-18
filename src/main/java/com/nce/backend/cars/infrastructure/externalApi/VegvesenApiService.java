package com.nce.backend.cars.infrastructure.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

//'Vegvesen' is the name for the Norwegian road inspection agency.
@Service
@RequiredArgsConstructor
public class VegvesenApiService implements ExternalApiService {

    private final WebClient webClient;

    @Value("${authorization.token}")
    private String authToken;

    @Override
    public ApiCarData fetchCarData(String registrationNumber) {

        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                        .queryParam("kjennemerke", registrationNumber)
                        .build()
                )
                .header("SVV-Authorization", authorizationToken)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}

