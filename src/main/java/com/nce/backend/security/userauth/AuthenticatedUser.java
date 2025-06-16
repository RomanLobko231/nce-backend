package com.nce.backend.security.userauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class AuthenticatedUser extends User {

    private UUID id;

    private boolean isAccountLocked;

    public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID id, boolean isAccountLocked) {
        super(username, password, authorities);
        this.id = id;
        this.isAccountLocked = isAccountLocked;
    }
}
