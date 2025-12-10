package com.cosmocats.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.api-key")
@Data
public class ApiKeyProperties {

    /**
     * Імʼя заголовка, з якого читаємо API key (X-API-KEY)
     */
    private String headerName;

    /**
     * Сам "секрет" / валідний API key
     */
    private String secret;

    /**
     * Імʼя користувача, під яким аутентифікуємо запит з валідним API key
     */
    private String username;

    /**
     * Роль, яку видаємо (наприклад, ADMIN або USER)
     */
    private String role;
}
