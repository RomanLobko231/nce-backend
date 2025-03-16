package com.nce.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTService {

    private final String SECRET_KEY = "N0ZCcWNFY3E3OTZxRGNxNzVjcUI4MzE2RkVxd0Y1OHF3cUQ0ODdxdzY3QzVxdzE=";

    private final long EXPIRATION_TIME = 86400000;

    private final String ISSUER = "NCE_BACKEND";

    public String generateJWT(UUID userId, String email) {
        return Jwts
                .builder()
                .issuer(ISSUER)
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return parseClaims(token).get("email").toString();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(
                parseClaims(token).getSubject()
        );
    }

    public boolean isTokenValid(String token ){
        return parseClaims(token).getExpiration().after(new Date());
    }

    public Claims parseClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
