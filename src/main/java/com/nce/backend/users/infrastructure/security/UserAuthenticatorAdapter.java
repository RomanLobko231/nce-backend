package com.nce.backend.users.infrastructure.security;

import com.nce.backend.security.SecurityProvider;
import com.nce.backend.users.application.port.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticatorAdapter implements Authenticator {

    private final SecurityProvider securityProvider;

    @Override
    public String generateHash(String rawPassword) {
        return securityProvider.generateHash(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return securityProvider.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateTokenFrom(String email) {
        return securityProvider.generateTokenFrom(email);
    }
}
