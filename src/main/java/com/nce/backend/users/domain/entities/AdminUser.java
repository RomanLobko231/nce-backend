package com.nce.backend.users.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AdminUser extends User {

    private String fallbackEmail;

    @Override
    protected void updateFieldsFrom(User source) {
        AdminUser other = (AdminUser) source;

        if (other.getName() != null) this.setName(other.getName());
        if (other.getEmail() != null) this.setEmail(other.getEmail());
        if (other.getPhoneNumber() != null) this.setPhoneNumber(other.getPhoneNumber());
        if (other.getFallbackEmail() != null) this.setFallbackEmail(other.getFallbackEmail());
    }

    @Override
    public void setAccountLock(boolean isAccountLocked) {
        this.setAccountLocked(isAccountLocked);
    }
}
