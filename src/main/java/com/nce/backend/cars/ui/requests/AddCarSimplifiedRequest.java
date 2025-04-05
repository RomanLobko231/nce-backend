package com.nce.backend.cars.ui.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCarSimplifiedRequest(

        @NotNull(message = "Owner ID cannot be null")
        UUID ownerId,

        @NotBlank(message = "Registration number should not be blank")
        @NotNull(message = "Registration number cannot be null")
        String registrationNumber,

        @NotNull(message = "KMs cannot be null")
        @Min(value = 0, message = "Kilometers value cannot be less than 0")
        Integer kilometers
) {
}
