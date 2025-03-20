package com.nce.backend.users.ui.requests;

import com.nce.backend.users.domain.valueObjects.Address;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record RegisterSellerRequest(

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "Phone number cannot be null")
        @NotBlank(message = "phone number cannot be blank")
        String phoneNumber,

        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password should be at least 8 symbols long")
        String password,

        @NotNull(message = "Address cannot be null")
        Address address
) {
}
