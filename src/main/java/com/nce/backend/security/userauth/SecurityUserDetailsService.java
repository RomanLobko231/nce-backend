package com.nce.backend.security.userauth;

import com.nce.backend.users.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class SecurityUserDetailsService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public AuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserFacade.AuthenticatedUserDTO userDTO = userFacade.getUserByEmail(username);

        return new AuthenticatedUser(
                userDTO.email(),
                userDTO.password(),
                List.of(new SimpleGrantedAuthority("ROLE_" + userDTO.role())),
                userDTO.id(),
                userDTO.isAccountLocked()
        );
    }
}
