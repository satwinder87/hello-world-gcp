package com.example.helloworldgcp.config;

import com.google.auth.oauth2.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import java.time.Instant;

@Configuration
public class SpringSecurityConfig {

    private String audience;

    public SpringSecurityConfig(@Value("${spring.cloud.gcp.security.iap.audience}") String audience) {
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
                                try {

                                    if (token == null) {
                                        throw new AuthorizationServiceException("Missing token!");
                                    }

                                    var verifier =
                                            TokenVerifier.newBuilder()
                                                    .setAudience(audience)
                                                    .setIssuer("https://cloud.google.com/iap")
                                                    .build();
                                    var signed = verifier.verify(token);
                                    var payload = signed.getPayload();
                                    return new Jwt(
                                            token,
                                            Instant.ofEpochSecond(payload.getIssuedAtTimeSeconds()),
                                            Instant.ofEpochSecond(payload.getExpirationTimeSeconds()),
                                            signed.getHeader(),
                                            payload);
                                } catch (TokenVerifier.VerificationException e) {
                                    throw new AuthorizationServiceException("Unauthorized", e);
                                }
                            }
                        })
                .and()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());;
        return http.build();
    }
}
