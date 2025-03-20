package com.nce.backend.users.ui.responses;

import com.nce.backend.users.domain.valueObjects.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class SellerUserResponse extends UserResponse {

    private Address address;

    private List<UUID> carIds;
}
