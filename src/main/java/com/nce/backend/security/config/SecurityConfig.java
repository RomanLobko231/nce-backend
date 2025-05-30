package com.nce.backend.security.config;

import com.nce.backend.security.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebSocketSecurity
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
                        .requestMatchers(GET, "/ws-auction/**", "/ws-auction").permitAll()
                        .requestMatchers(GET, "/api/v1/cars").permitAll()
                        .requestMatchers(GET, "/api/v1/cars/exists").permitAll()
                        .requestMatchers(POST, "/api/v1/cars/add_simplified").permitAll()
                        .requestMatchers(POST, "/api/v1/cars/add_complete").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(GET, "/api/v1/cars/**").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY", "BUYER_REPRESENTATIVE")
                        .requestMatchers(DELETE, "/api/v1/cars/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(POST, "/api/v1/cars/**").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(POST, "/api/v1/users/**", "/api/v1/users/login").permitAll()
                        .requestMatchers(GET, "/api/v1/users/**").authenticated()
                        .requestMatchers(GET, "/api/v1/users").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY")
                        .requestMatchers(PUT, "/api/v1/users").hasAnyRole("ADMIN", "SELLER", "BUYER_COMPANY")
                        .requestMatchers(DELETE, "/api/v1/users/**").authenticated()
                        .requestMatchers(GET, "/api/v1/auctions").authenticated()
                        .requestMatchers(PUT, "/api/v1/auctions").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/auctions").hasRole("ADMIN")
                        .requestMatchers(POST, "/api/v1/auctions/place-bid", "/api/v1/auctions/place-auto-bid").hasRole("BUYER_REPRESENTATIVE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/user/queue/errors", "/topic/**").authenticated()
                .simpDestMatchers("/app/**").hasRole("BUYER_REPRESENTATIVE")
                .anyMessage().authenticated();

        return messages.build();
    }

    @Bean(name = "csrfChannelInterceptor")
    ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                return message;
            }
        };
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
