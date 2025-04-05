package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.BuyerAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@JsonTypeName("BUYER")
public class UpdateBuyerRequest extends UpdateUserRequest{

    @NotNull(message = "Address cannot be null")
    private BuyerAddress address;

    @NotBlank(message = "Organisation name cannot be blank")
    @NotNull(message = "Organisation name cannot be null")
    private String organisationName;

    @NotBlank(message = "Organisation number cannot be blank")
    @NotNull(message = "Organisation number cannot be null")
    private String organisationNumber;

    @NotNull(message = "Organisation licences cannot be blank")
    private List<String> organisationLicenceURLs;
}
