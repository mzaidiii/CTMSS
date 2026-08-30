package org.personal.ctmss.config;

import org.personal.ctmss.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/trails/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/trails/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/trails/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.DELETE, "/api/trails/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/adverse-events/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/adverse-events/**").hasAnyRole("ADMIN", "PV", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/adverse-events/*/causality-assessment").hasAnyRole("ADMIN", "PV")
                        .requestMatchers(HttpMethod.PATCH, "/api/adverse-events/*/status").hasAnyRole("ADMIN", "PV")
                        .requestMatchers("/fhir/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}