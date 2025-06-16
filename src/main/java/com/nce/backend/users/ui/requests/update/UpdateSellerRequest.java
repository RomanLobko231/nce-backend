package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.ui.requests.address.ValidatedAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.*;

@Getter
@JsonTypeName("SELLER")
public class UpdateSellerRequest extends UpdateUserRequest {

    @NotNull(message = "Address cannot be null")
    @Valid
    private ValidatedAddress address;

    private Set<UUID> carIDs = new HashSet<>();
}
