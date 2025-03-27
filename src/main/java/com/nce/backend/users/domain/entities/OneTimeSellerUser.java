package com.nce.backend.users.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Setter
@Getter
@SuperBuilder
public class OneTimeSellerUser extends User{

    @Builder.Default
    private UUID carId = UUID.fromString("00000000-0000-0000-0000-000000000000");
}
