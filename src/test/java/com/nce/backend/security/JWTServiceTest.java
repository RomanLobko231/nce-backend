package com.nce.backend.security;

import com.nce.backend.security.jwt.JWTService;
import com.nce.backend.users.domain.valueObjects.Role;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTServiceTest {

    @Autowired
    JWTService jwtService;

    @Test
    void testGenerateToken_CheckClaims(){
        String email = "email";

        String token = jwtService.generateJWT(email);
        Claims extractedClaims = jwtService.parseClaims(token);
        String extractedEmail = jwtService.extractEmail(token);
        boolean isValid = jwtService.isTokenValid(token);

        assertNotNull(token);
        assertNotNull(extractedClaims);
        assertEquals(extractedEmail, email);
        assertTrue(isValid);
    }
}
