package com.nce.backend.logging.infrastructure.security;

import com.nce.backend.logging.application.port.Authenticator;
import com.nce.backend.security.SecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoggingAuthenticatorAdapter implements Authenticator {

    private final SecurityProvider securityProvider;

    @Override
    public String getCurrentUserName() {
        return securityProvider
                .getCurrentUsername()
                .orElse("AnonymousUser");
    }

    @Override
    public UUID getCurrentUserId() {
        return securityProvider
                .getCurrentUserId()
                .orElse(null);
    }
}
