package com.nce.backend.users;

import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    public AuthenticatedUserDTO getUserByEmail(String email) {
        User fetchedUser = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with email %s was not found".formatted(email))
                );

        return AuthenticatedUserDTO
                .builder()
                .id(fetchedUser.getId())
                .email(fetchedUser.getEmail())
                .password(fetchedUser.getPassword())
                .role(fetchedUser.getRole().name())
                .isAccountLocked(fetchedUser.isAccountLocked())
                .build();
    }

    @Builder
    public record AuthenticatedUserDTO(
            UUID id,
            String email,
            String password,
            String role,
            boolean isAccountLocked
    ) {
    }
}
