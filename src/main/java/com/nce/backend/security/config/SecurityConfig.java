package com.nce.backend.security.config;

import com.nce.backend.security.jwt.JWTAuthenticationFilter;
import com.nce.backend.security.user.AuthenticatedUser;
import com.nce.backend.users.ui.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{'${security.allowed-origins}'.split(',')}")
    private List<String> ALLOWED_ORIGINS;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/ws-auction/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "api/v1/cars").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/cars/exists").permitAll()
                                .requestMatchers(HttpMethod.POST, "api/v1/cars/add_simplified").permitAll()
                                .requestMatchers(HttpMethod.POST, "api/v1/cars/add_complete").hasAnyRole("ADMIN", "SELLER")
                                .requestMatchers(HttpMethod.GET, "api/v1/cars/**").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY", "BUYER_REPRESENTATIVE")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/cars/**").hasAnyRole("ADMIN", "SELLER")
                                .requestMatchers(HttpMethod.POST, "api/v1/cars/**").hasAnyRole("ADMIN", "SELLER")
                                .requestMatchers(HttpMethod.POST, "api/v1/users/**", "api/v1/users/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/users").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY")
                                .requestMatchers(HttpMethod.PUT, "api/v1/users").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/users/**").hasAnyRole("ADMIN", "BUYER_COMPANY")
                                .requestMatchers(HttpMethod.GET, "api/v1/auctions").authenticated()
                                .requestMatchers(HttpMethod.PUT, "api/v1/auctions").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "api/v1/auctions").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "api/v1/auctions").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
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
