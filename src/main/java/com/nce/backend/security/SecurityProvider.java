package com.nce.backend.security;

import com.nce.backend.security.jwt.JWTService;
import com.nce.backend.security.userauth.AuthenticatedUser;
import com.nce.backend.users.application.port.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityProvider {

    private final PasswordEncoder passwordEncoder;
    private final JWTService tokenService;

    public String generateHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateTokenFrom(String email) {
        return tokenService.generateJWT(email);
    }

    public Optional<UUID> getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof AuthenticatedUser user) {
                return Optional.ofNullable(user.getId());
            }
        }

        return Optional.empty();
    }

    public Optional<String> getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof AuthenticatedUser user) {
                return Optional.ofNullable(user.getUsername());
            }
        }

        return Optional.empty();
    }
}
