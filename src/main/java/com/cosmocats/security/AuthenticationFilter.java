package com.cosmocats.security;

import com.cosmocats.config.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyProperties apiKeyProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Дістаємо API key із заголовка
        String apiKey = request.getHeader(apiKeyProperties.getHeaderName());

        // 2. Якщо заголовка немає — просто йдемо далі (може прийде Bearer JWT)
        if (apiKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Якщо API key не співпав із secret → 401
        if (!apiKeyProperties.getSecret().equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"Invalid API key\"}");
            return;
        }

        // 4. Якщо все ок — створюємо Authentication і кладемо в контекст
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        apiKeyProperties.getUsername(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + apiKeyProperties.getRole()))
                );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
