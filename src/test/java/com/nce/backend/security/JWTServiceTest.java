package com.nce.backend.security;

import com.nce.backend.security.jwt.JWTService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTServiceTest {

    @Autowired
    JWTService jwtService;

    @Test
    void testGenerateToken_CheckClaims(){
        UUID userId = UUID.randomUUID();
        String email = "email";

        String token = jwtService.generateJWT(userId, email);
        Claims claims = jwtService.parseClaims(token);
        UUID extractedId = jwtService.extractUserId(token);
        String extractedEmail = jwtService.extractEmail(token);
        boolean isValid = jwtService.isTokenValid(token);

        assertNotNull(token);
        assertNotNull(claims);
        assertEquals(extractedId, userId);
        assertEquals(extractedEmail, email);
        assertTrue(isValid);
    }
}
