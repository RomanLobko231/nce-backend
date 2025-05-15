package com.nce.backend.cars.ui.responses;

import lombok.Builder;
import org.hibernate.Internal;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record CarResponse(

        UUID id,

        String registrationNumber,

        Integer kilometers,

        String make,

        String model,

        LocalDate firstTimeRegisteredInNorway,

        String engineType,

        Integer engineVolume,

        String bodywork,

        Integer numberOfSeats,

        Integer numberOfDoors,

        String color,

        String gearboxType,

        String operatingMode,

        Integer weight,

        LocalDate nextEUControl,

        UUID ownerId,

        String status,

        String additionalInformation,

        Integer expectedPrice,

        List<String> imagePaths
) {
}
