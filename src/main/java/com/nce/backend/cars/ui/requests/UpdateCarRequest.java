package com.nce.backend.cars.ui.requests;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
        Double engineVolume,

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
        UUID ownerId,

        @Min(value = 0, message = "Price must be no less than 0")
        @NotNull(message = "Price cannot be null")
        Integer expectedPrice,

        String additionalInformation,

        List<String> imagePaths
) {
}
