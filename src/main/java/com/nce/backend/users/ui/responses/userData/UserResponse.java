package com.nce.backend.users.ui.responses.userData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
public abstract class UserResponse {

    private String email;

    private String name;

    private String phoneNumber;

    private String role;

    private UUID id;

    @JsonProperty(value = "accountLocked")
    private boolean isAccountLocked;
}
