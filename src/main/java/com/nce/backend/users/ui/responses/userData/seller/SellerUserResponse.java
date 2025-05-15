package com.nce.backend.users.ui.responses.userData.seller;

import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.ui.responses.userData.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class SellerUserResponse extends UserResponse {

    private Address address;

    private Set<UUID> carIds;
}
