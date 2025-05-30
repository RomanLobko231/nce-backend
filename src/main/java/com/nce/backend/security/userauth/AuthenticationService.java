package com.nce.backend.security.userauth;

import com.nce.backend.common.exception.InvalidTokenException;
import com.nce.backend.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static io.micrometer.common.util.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JWTService jwtService;
    private final SecurityUserService userService;

    //only returns Authentication object - must be set manually by client code
    public UsernamePasswordAuthenticationToken authenticateWith(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid token");
        }

        final String email = jwtService.extractEmail(token);

        if(isBlank(email)) {
            throw new InvalidTokenException("Email provided in token is invalid.");
        }

        AuthenticatedUser user =  userService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
