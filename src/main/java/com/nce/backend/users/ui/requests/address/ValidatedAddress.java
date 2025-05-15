package com.nce.backend.users.ui.requests.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ValidatedAddress(
        @NotNull(message = "Street address cannot be null")
        @NotBlank(message = "Street address cannot be blank")
        String streetAddress,

        @NotNull(message = "Postal location cannot be null")
        @NotBlank(message = "Postal location cannot be blank")
        String postalLocation,

        @NotNull(message = "Postal code cannot be null")
        @NotBlank(message = "Postal code cannot be blank")
        String postalCode
) {
}
