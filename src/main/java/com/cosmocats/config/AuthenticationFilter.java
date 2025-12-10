package com.cosmocats.security;

import com.cosmocats.config.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyProperties apiKeyProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader(apiKeyProperties.getHeaderName());

        // Якщо хедеру немає — просто пропускаємо далі (JWT/інші механізми)
        if (requestApiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Якщо API key валідний — ставимо аутентифікацію в контекст
        if (apiKeyProperties.getSecret().equals(requestApiKey)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    apiKeyProperties.getUsername(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(apiKeyProperties.getRole()))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            // Якщо ключ невалідний — 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
        }
    }
}
