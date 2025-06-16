package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.valueObjects.ApiCarData;

import java.util.concurrent.CompletableFuture;

public interface ExternalApiService {
    CompletableFuture<ApiCarData> fetchCarData(String registrationNumber);
}
