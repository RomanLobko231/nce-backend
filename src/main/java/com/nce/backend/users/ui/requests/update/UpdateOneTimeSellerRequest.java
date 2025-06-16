package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonTypeName("ONE_TIME_SELLER")
public class UpdateOneTimeSellerRequest extends UpdateUserRequest {

    @NotNull(message = "Car Id cannot be null")
    private UUID carId;
}
