package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.nce.backend.users.domain.valueObjects.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@JsonTypeName("SELLER")
public class UpdateSellerRequest extends UpdateUserRequest {

    @NotNull(message = "Address cannot be null")
    private Address address;

    private List<UUID> carIDs = new ArrayList<>();
}
