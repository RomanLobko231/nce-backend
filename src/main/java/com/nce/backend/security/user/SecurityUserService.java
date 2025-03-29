package com.nce.backend.security.user;

import com.nce.backend.users.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public AuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserFacade.AuthenticatedUserDTO userDTO = userFacade.getUserByEmail(username);

        return new AuthenticatedUser(
                userDTO.email(),
                userDTO.password(),
                List.of(new SimpleGrantedAuthority(userDTO.role())),
                userDTO.id(),
                userDTO.isAccountLocked()
        );
    }
}
