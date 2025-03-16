package com.nce.backend.users.domain.entities;

import com.nce.backend.users.domain.valueObjects.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@SuperBuilder
public class SellerUser extends User{

    private Address sellerAddress;

    @Builder.Default
    private List<UUID> carIDs = new ArrayList<>();

    public void addCarId(UUID carId) {
        carIDs.add(carId);
    }
}
