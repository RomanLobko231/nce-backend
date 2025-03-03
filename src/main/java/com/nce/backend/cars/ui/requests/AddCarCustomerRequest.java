package com.nce.backend.cars.ui.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public record AddCarCustomerRequest(

        @NotBlank(message = "Name should not be blank")
        @NotNull(message = "Name cannot be null")
        String ownerName,

        @NotBlank(message = "Phone number should not be blank")
        @NotNull(message = "Phone number cannot be null")
        String phoneNumber,

        @NotBlank(message = "Registration number should not be blank")
        @NotNull(message = "Registration number cannot be null")
        String registrationNumber,

        @Nullable
        String email,

        @NotNull(message = "KMs cannot be null")
        @Min(value = 0, message = "Kilometers value cannot be less than 0")
        Integer kilometers
) {
}
