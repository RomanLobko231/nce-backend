package com.nce.backend.cars.infrastructure.externalApi;

import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import org.springframework.stereotype.Service;

//'Vegvesen' is the name for the Norwegian road inspection agency
@Service
public class VegvesenApiService implements ExternalApiService {

    @Override
    public ApiCarData fetchCarData(String registrationNumber) {
        return null;
    }
}
