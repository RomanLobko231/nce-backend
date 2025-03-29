package com.nce.backend.security.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

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
