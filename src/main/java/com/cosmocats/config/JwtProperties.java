package com.cosmocats.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    /**
     * Секрет для підпису JWT (має бути довгий рядок).
     */
    private String secret;

    /**
     * Алгоритм, наприклад HS256.
     */
    private String algorithm;
}
