package com.nce.backend.users.ui.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        String password,

        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        String email
) {
}
