package com.example.practica2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration 
public class SecurityConfig {

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtService jwtService) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);

        httpSecurity
            .authorizeHttpRequests((auth) -> {
                auth
                    .requestMatchers(HttpMethod.POST, "/api/noauth/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated(); 
            })
            .sessionManagement((session) -> {
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .csrf((csrf) -> {
                csrf
                    .disable();
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();

    }

}
