package com.nce.backend.cars.infrastructure.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

//'Vegvesen' is the name for the Norwegian road inspection agency.
@Service
@RequiredArgsConstructor
public class VegvesenApiService implements ExternalApiService {


    private final WebClient webClient;

    private final VegvesenApiJsonMapper jsonMapper;

    @Value("${authorization.token}")
    private String authToken;

    @Override
    public ApiCarData fetchCarData(String registrationNumber) {
        Mono<JsonNode> apiResponse = webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .queryParam("kjennemerke", registrationNumber)
                                .build()
                )
                .header("SVV-Authorization", authToken)
                .retrieve()
                .bodyToMono(JsonNode.class);

        return apiResponse
                .map(jsonMapper::mapFromJson)
                .onErrorReturn(
                        ApiCarData.builder()
                                .firstTimeRegisteredInNorway(LocalDate.parse("1111-11-11"))
                                .nextEUControl(LocalDate.parse("1111-11-11"))
                                .model("N/A")
                                .make("N/A")
                                .color("N/A")
                                .bodywork("N/A")
                                .engineType("N/A")
                                .weight(0)
                                .engineVolume(0)
                                .numberOfDoors(0)
                                .numberOfSeats(0)
                                .operatingMode(OperatingMode.OTHER)
                                .gearboxType(GearboxType.OTHER)
                                .build())
                .block();
    }
}

