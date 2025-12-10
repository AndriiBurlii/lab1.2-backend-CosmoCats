package org.example.cosmocats.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CosmoCatsTokenAuthenticationFilter extends OncePerRequestFilter {

    // 🔑 Тут "умовні" токени, якими будеш користуватись у Postman / тестах
    public static final String USER_TOKEN = "cosmo-user-token";
    public static final String ADMIN_TOKEN = "cosmo-admin-token";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            UsernamePasswordAuthenticationToken authentication = null;

            if (USER_TOKEN.equals(token)) {
                authentication = buildAuthentication(
                        "user@cosmocats.space",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
            } else if (ADMIN_TOKEN.equals(token)) {
                authentication = buildAuthentication(
                        "admin@cosmocats.space",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            }

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthentication(
            String username,
            List<GrantedAuthority> authorities
    ) {
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // тут все, що має бути публічним і без токена
        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.equals("/actuator/health");
    }
}
