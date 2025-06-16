package com.nce.backend.users.domain.entities;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.nce.backend.users.domain.valueObjects.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private Role role;

    @Setter(AccessLevel.PROTECTED)
    private boolean isAccountLocked;

    public final void applyChangesFrom(User source){
        if (source == null) {
            throw new IllegalArgumentException("Cannot update user from null");
        }
        if(!this.getClass().equals(source.getClass())){
            throw new IllegalArgumentException("Provided changes are not from user type " + this.getClass());
        }
        updateFieldsFrom(source);
    }

    protected abstract void updateFieldsFrom(User source);

    public abstract void setAccountLock(boolean isAccountLocked);
}
