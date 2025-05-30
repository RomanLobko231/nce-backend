package com.nce.backend.users.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerRepresentativeUser extends User {

    @Builder.Default
    private Set<UUID> savedCarIds = new HashSet<>();

    private UUID buyerCompanyId;

    public void addNewSavedCar(UUID carId) {
        if(carId != null) {
            savedCarIds.add(carId);
        }
    }

    @Override
    protected void updateFieldsFrom(User source) {
        if (source.getName() != null) this.setName(source.getName());
        if (source.getEmail() != null) this.setEmail(source.getEmail());
        if (source.getPhoneNumber() != null) this.setPhoneNumber(source.getPhoneNumber());
    }

    @Override
    public void setAccountLock(boolean isAccountLocked) {
        this.setAccountLocked(isAccountLocked);
    }
}
