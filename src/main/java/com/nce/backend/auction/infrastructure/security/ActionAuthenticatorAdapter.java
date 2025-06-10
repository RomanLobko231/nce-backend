package com.nce.backend.auction.infrastructure.security;

import com.nce.backend.auction.application.port.Authenticator;
import com.nce.backend.auction.exceptions.InvalidBidException;
import com.nce.backend.security.SecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ActionAuthenticatorAdapter implements Authenticator {

    private final SecurityProvider securityProvider;

    @Override
    public UUID getCurrentUserId() {
        return securityProvider
                .getCurrentUserId()
                .orElseThrow(
                        () -> new InvalidBidException("Current user id is not present. Cannot proceed.")
                );
    }
}
