package com.nce.backend.users.domain.entities;

import com.nce.backend.users.domain.valueObjects.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class User {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private Role role;

    private boolean isAccountLocked;
}
