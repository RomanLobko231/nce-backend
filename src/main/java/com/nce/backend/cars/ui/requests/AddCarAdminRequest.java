package com.nce.backend.cars.ui.requests;

import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AddCarAdminRequest(


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

        OwnerInfo ownerInfo,

        String status,

        String additionalInformation,

        List<String> imagePaths
) {
}
