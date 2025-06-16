package com.nce.backend.users.application.port;


public interface Authenticator {
    String generateHash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
    String generateTokenFrom(String email);
}
