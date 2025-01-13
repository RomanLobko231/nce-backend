package com.nce.backend.cars.ui.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddCarRequest(

        @NotBlank(message = "Name should not be blank")
        @NotNull(message = "Name cannot be null")
        String sellerName,

        @NotBlank(message = "Phone number should not be blank")
        @NotNull(message = "Phone number cannot be null")
        String phoneNumber,

        @NotBlank(message = "Registration number should not be blank")
        @NotNull(message = "Registration number cannot be null")
        String carRegistrationNumber
) {
}
