package com.nce.backend.cars.infrastructure.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

//'Vegvesen' is the name for the Norwegian road inspection agency.
@Service
@RequiredArgsConstructor
@Slf4j
public class VegvesenApiService implements ExternalApiService {

    private final WebClient webClient;
    private final VegvesenApiJsonMapper jsonMapper;

    @Value("${vegvesen.api-key}")
    private String API_KEY;

    @Override
    @Async
    public CompletableFuture<ApiCarData> fetchCarData(String registrationNumber) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .queryParam("kjennemerke", registrationNumber)
                                .build()
                )
                .header("SVV-Authorization", API_KEY)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonMapper::mapFromJson)
                .switchIfEmpty(
                        Mono.fromCallable(() -> {
                            log.warn("No content (204) for reg: {}", registrationNumber);
                            return getDefaultCarData();
                        })
                )
                .onErrorReturn(
                        throwable -> {
                            log.warn("Error fetching car data for reg: {}", registrationNumber, throwable);
                            return true;
                        }, getDefaultCarData()
                )
                .toFuture();
    }

    private ApiCarData getDefaultCarData() {
        return ApiCarData.builder()
                .firstTimeRegisteredInNorway(LocalDate.parse("1111-11-11"))
                .nextEUControl(LocalDate.parse("1111-11-11"))
                .model("N/A")
                .make("N/A")
                .color("N/A")
                .bodywork("N/A")
                .engineType("N/A")
                .weight(0)
                .engineVolume(0d)
                .numberOfDoors(0)
                .numberOfSeats(0)
                .operatingMode(OperatingMode.OTHER)
                .gearboxType(GearboxType.OTHER)
                .build();
    }

}

