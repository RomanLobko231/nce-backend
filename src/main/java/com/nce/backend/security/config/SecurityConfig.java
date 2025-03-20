package com.nce.backend.security.config;

import com.nce.backend.security.jwt.JWTAuthenticationFilter;
import com.nce.backend.users.ui.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserController userController) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"api/v1/cars").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/cars/{id}").hasAnyRole("ADMIN", "SELLER", "BUYER")
                        .requestMatchers(HttpMethod.DELETE,"api/v1/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"api/v1/users/**", "api/v1/cars/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/v1/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"api/v1/users").hasAnyRole("ADMIN", "SELLER", "BUYER")
                        .requestMatchers(HttpMethod.GET,"api/v1/users/test").access((authentication, request) ->
                                authentication.get().getPrincipal() instanceof UserDetails userDetails &&
                                        !userDetails.isAccountNonLocked() &&
                                        userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BUYER")) ?
                                        new AuthorizationDecision(true) : new AuthorizationDecision(false)
                        )
                        .requestMatchers(HttpMethod.GET,"api/v1/users/test2").access((authentication, request) ->
                                authentication.get().getPrincipal() instanceof UserDetails userDetails &&
                                        userDetails.isAccountNonLocked() &&
                                        userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BUYER")) ?
                                        new AuthorizationDecision(true) : new AuthorizationDecision(false)
                        )
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001", "https://norwaycarexport.netlify.app"));
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
