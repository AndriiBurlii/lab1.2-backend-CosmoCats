package org.example.cosmocats.config;

import org.example.cosmocats.security.CosmoCatsTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CosmoCatsTokenAuthenticationFilter tokenFilter;

    public SecurityConfig(CosmoCatsTokenAuthenticationFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // без state, бо токени
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // csrf можна відрубити для REST
                .csrf(AbstractHttpConfigurer::disable)

                // хто куди має доступ
                .authorizeHttpRequests(auth -> auth
                        // повністю відкриті технічні ендпоінти
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/health"
                        ).permitAll()

                        // приклад: публічний перегляд товарів/категорій
                        .requestMatchers(HttpMethod.GET,
                                "/api/categories/**",
                                "/api/products/**"
                        ).permitAll()

                        // все інше вимагає авторизації з Bearer токеном
                        .anyRequest().authenticated()
                )

                // ставимо наш Bearer-фільтр перед стандартним
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)

                // 401 для неавторизованих
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }
}
