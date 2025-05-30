package com.nce.backend.users.domain.entities;

import com.nce.backend.users.domain.valueObjects.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Setter
@Getter
@SuperBuilder
public class SellerUser extends User{

    private Address sellerAddress;

    @Builder.Default
    private Set<UUID> carIDs = new HashSet<>();

    public void addCarId(UUID carId) {
        if(carId != null) {
            carIDs.add(carId);
        }
    }

    @Override
    protected void updateFieldsFrom(User source) {
        SellerUser other = (SellerUser) source;

        if (other.getEmail() != null) this.setEmail(other.getEmail());
        if (other.getName() != null) this.setName(other.getName());
        if (other.getPhoneNumber() != null) this.setPhoneNumber(other.getPhoneNumber());
        if (other.getSellerAddress() != null) this.setSellerAddress(other.getSellerAddress());
    }

    @Override
    public void setAccountLock(boolean isAccountLocked) {
        this.setAccountLocked(isAccountLocked);
    }
}
