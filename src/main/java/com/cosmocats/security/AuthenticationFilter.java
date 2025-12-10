package com.cosmocats.security;

import com.cosmocats.config.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyProperties properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader(properties.getHeaderName());
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (apiKey == null && authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (apiKey != null) {
            handleApiKeyAuth(apiKey);
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            handleBearerToken(authHeader.substring(7));
        }

        filterChain.doFilter(request, response);
    }

    private void handleApiKeyAuth(String apiKey) {
        if (!properties.getValidKey().equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid API key\"}");
            return;
        }

        AbstractAuthenticationToken auth = new AbstractAuthenticationToken(
                List.of(new SimpleGrantedAuthority("ROLE_API_USER"))
        ) {
            @Override
            public Object getCredentials() {
                return apiKey;
            }

            @Override
            public Object getPrincipal() {
                return "api-key-user";
            }
        };

        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void handleBearerToken(String token) {
        // тут можна щось робити з JWT, але для спрощення зробимо мок
        AbstractAuthenticationToken auth = new AbstractAuthenticationToken(
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ) {
            @Override
            public Object getCredentials() {
                return token;
            }

            @Override
            public Object getPrincipal() {
                return "jwt-user";
            }
        };

        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
