package com.nce.backend.users.ui.responses.userData.seller;

import com.nce.backend.users.ui.responses.userData.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class OneTimeSellerUserResponse extends UserResponse {
     private UUID carId;
}
