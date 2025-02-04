package com.nce.backend.cars.ui.requests;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;

import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public record UpdateCarRequest(

        @NotNull(message = "ID cannot be null")
        UUID id,

        String registrationNumber,

        @PositiveOrZero(message = "Kilometers cannot be less then 0")
        Integer kilometers,

        String make,

        String model,

        @PastOrPresent(message = "Car cannot be registered in the future")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate firstTimeRegisteredInNorway,

        String engineType,

        @PositiveOrZero(message = "Engine volume cannot be less then 0")
        Integer engineVolume,

        String bodywork,

        @PositiveOrZero(message = "Seats cannot be less then 0")
        Integer numberOfSeats,

        @PositiveOrZero(message = "Doors cannot be less then 0")
        Integer numberOfDoors,

        String color,

        String gearboxType,

        String operatingMode,

        @PositiveOrZero(message = "Weight cannot be less then 0")
        Integer weight,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate nextEUControl,

        @NotNull
        OwnerInfo ownerInfo,

        String status,

        String additionalInformation,

        List<String> imagePaths
) {
}
