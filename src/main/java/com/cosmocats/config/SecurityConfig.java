package org.example.cosmocats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API => без сесій та форм
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // AUTH RULES
                .authorizeHttpRequests(auth -> auth
                        // swagger / docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ендпоїнти реєстрації/логіну, якщо є
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // публічні речі, якщо є (категорії / продукти тільки на GET)
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // наприклад, адмініка
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // усе інше – тільки аутентифіковані
                        .anyRequest().authenticated()
                )

                // OAuth2 Resource Server з Bearer Token (JWT)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
