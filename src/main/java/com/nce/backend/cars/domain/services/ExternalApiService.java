package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.valueObjects.ApiCarData;

public interface ExternalApiService {
    ApiCarData fetchCarData(String registrationNumber);
}
