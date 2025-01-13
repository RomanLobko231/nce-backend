package com.nce.backend.cars.ui.responses;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import lombok.Builder;

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

        GearboxType gearboxType,

        OperatingMode operatingMode,

        Integer weight,

        LocalDate nextEUControl,

        OwnerInfo ownerInfo,

        Status status,

        String additionalInformation,

        List<String> imagePaths
) {
}
