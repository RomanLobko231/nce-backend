package com.nce.backend.security;

import com.nce.backend.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
