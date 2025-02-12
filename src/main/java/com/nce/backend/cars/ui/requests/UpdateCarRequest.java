package com.nce.backend.cars.ui.requests;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;

import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.validation.constraints.*;
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
        @NotNull
        Integer kilometers,

        @NotBlank(message = "Make cannot be null or blank")
        String make,

        @NotBlank(message = "Model cannot be null or blank")
        String model,

        @PastOrPresent(message = "Car cannot be registered in the future")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate firstTimeRegisteredInNorway,

        @NotBlank(message = "Engine type cannot be null or blank")
        String engineType,

        @PositiveOrZero(message = "Engine volume cannot be less then 0")
        @NotNull
        Integer engineVolume,

        @NotBlank(message = "Bodywork cannot be null or blank")
        String bodywork,

        @PositiveOrZero(message = "Seats cannot be less then 0")
        @NotNull
        Integer numberOfSeats,

        @PositiveOrZero(message = "Doors cannot be less then 0")
        @NotNull
        Integer numberOfDoors,

        @NotBlank(message = "Color cannot be null or blank")
        String color,

        @NotBlank(message = "Gearbox type cannot be null or blank")
        String gearboxType,

        @NotBlank(message = "Operating mode cannot be null or blank")
        String operatingMode,

        @PositiveOrZero(message = "Weight cannot be less then 0")
        @NotNull
        Integer weight,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate nextEUControl,

        @NotNull
        OwnerInfo ownerInfo,

        @NotBlank(message = "Status cannot be null or blank")
        String status,

        String additionalInformation,

        List<String> imagePaths
) {
}
