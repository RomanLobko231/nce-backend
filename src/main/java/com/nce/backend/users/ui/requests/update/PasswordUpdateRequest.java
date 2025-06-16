package com.nce.backend.users.ui.requests.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public record PasswordUpdateRequest(
        @NotBlank(message = "Password is empty")
        @Size(min = 8, message = "Password must be at least 8 symbols long")
        String password
) {
}
