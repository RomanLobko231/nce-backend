package com.nce.backend.security;

import com.nce.backend.auction.AuctionSecurityService;
import com.nce.backend.security.jwt.JWTService;
import com.nce.backend.security.userauth.AuthenticatedUser;
import com.nce.backend.users.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityProvider implements Authenticator, AuctionSecurityService {

    private final PasswordEncoder passwordEncoder;

    private final JWTService tokenService;

    @Override
    public String generateHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateTokenFrom(String email) {
        return tokenService.generateJWT(email);
    }

    @Override
    public UUID getCurrentUserId() {
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getId();
    }

}
