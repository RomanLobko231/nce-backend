package com.nce.backend.users;

import com.nce.backend.security.userauth.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface Authenticator {
    String generateHash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
    String generateTokenFrom(String email);
}
