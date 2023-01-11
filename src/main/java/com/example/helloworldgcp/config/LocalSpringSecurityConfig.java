package com.example.helloworldgcp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import java.time.Instant;
import java.util.Map;

public class LocalSpringSecurityConfig {

    private String audience;

    public LocalSpringSecurityConfig(@Value("${spring.cloud.gcp.security.iap.audience}") String audience) {
        this.audience = audience;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/ping").permitAll()
                .anyRequest()
                .authenticated()
                .and().oauth2ResourceServer()
                .jwt()
                .decoder(
                        new JwtDecoder() {
                            @Override
                            public Jwt decode(String token) throws JwtException {
                                    if (token == null) {
                                        throw new AuthorizationServiceException("Missing token!");
                                    }

                                return new Jwt(
                                        token,
                                        Instant.now(),
                                        Instant.now().plusSeconds(30),
                                        Map.of("alg", "none"),
                                        Map.of("email", "test@sebx.se", "aud", audience));

                            }
                        })
                .and()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());;
        return http.build();
    }
}
