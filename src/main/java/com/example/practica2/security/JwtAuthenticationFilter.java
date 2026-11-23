package com.example.practica2.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String peticion = request.getHeader("Authorization");

        if (peticion != null && !peticion.isBlank()) {
            if (peticion.startsWith("Bearer ")) {
                peticion = peticion.substring(7);

                if (jwtService.validateToken(peticion)) {
                    String usuario = jwtService.getUsernameFromToken(peticion);
                    if (usuario != null && !usuario.isBlank()) {
                        UsernamePasswordAuthenticationToken autenticado = new UsernamePasswordAuthenticationToken(
                                usuario, null, Collections.emptyList());
                        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
                        contexto.setAuthentication(autenticado);
                        SecurityContextHolder.setContext(contexto);
                    }

                }
            }

        }

        filterChain.doFilter(request, response);

    }

}
