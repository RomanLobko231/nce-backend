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

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserFacade.AuthenticatedUserDTO userDTO = userFacade.getUserByEmail(username);

        return User
                .builder()
                .username(userDTO.email())
                .password(userDTO.password())
                .authorities(
                        new SimpleGrantedAuthority("ROLE_" + userDTO.role())
                )
                .accountLocked(userDTO.isAccountLocked())
                .build();
    }
}
