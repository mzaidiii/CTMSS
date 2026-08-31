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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        .requestMatchers("/actuator/**", "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/trails/**", "/api/trials/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/trails/**", "/api/trials/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/trails/**", "/api/trials/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.DELETE, "/api/trails/**", "/api/trials/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sites/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/sites/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.DELETE, "/api/sites/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/patients/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/visits/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/visits/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/visits/**").hasAnyRole("ADMIN", "PI")
                        .requestMatchers(HttpMethod.DELETE, "/api/visits/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/adverse-events/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/adverse-events/**").hasAnyRole("ADMIN", "PV", "PI")
                        .requestMatchers(HttpMethod.PATCH, "/api/adverse-events/*/causality-assessment").hasAnyRole("ADMIN", "PV")
                        .requestMatchers(HttpMethod.PATCH, "/api/adverse-events/*/status").hasAnyRole("ADMIN", "PV")
                        .requestMatchers("/api/kpis/**").authenticated()
                        .requestMatchers("/fhir/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}