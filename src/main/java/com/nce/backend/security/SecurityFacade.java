package com.nce.backend.security;

import com.nce.backend.security.jwt.JWTService;
import com.nce.backend.security.userauth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityFacade {

    private final PasswordEncoder passwordEncoder;

    private final JWTService tokenService;

    public String generateHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateToken(String email) {
        return tokenService.generateJWT(email);
    }

    public UUID getCurrentUserId() {
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getId();
    }

}
