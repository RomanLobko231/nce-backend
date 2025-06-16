package com.nce.backend.users.ui.requests.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterOneTimeSellerRequest(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "Phone number cannot be null")
        @NotBlank(message = "phone number cannot be blank")
        String phoneNumber
) {
}
