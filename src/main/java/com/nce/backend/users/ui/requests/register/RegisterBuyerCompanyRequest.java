package com.nce.backend.users.ui.requests.register;

import com.nce.backend.users.ui.requests.address.ValidatedBuyerCompanyAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterBuyerCompanyRequest(

        @NotNull(message = "Organisation Name cannot be null")
        @NotBlank(message = "Organisation Name cannot be blank")
        String organisationName,

        @NotNull(message = "Organisation Number cannot be null")
        @NotBlank(message = "Organisation Number cannot be blank")
        String organisationNumber,

        @NotNull(message = "Address cannot be null")
        @Valid
        ValidatedBuyerCompanyAddress organisationAddress,

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
        String password

) {
}
