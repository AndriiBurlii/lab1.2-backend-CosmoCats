package com.cosmocats.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security.api-key")
public class ApiKeyProperties {

    /**
     * Сам секретний ключ, який ти будеш передавати в хедері.
     */
    private String secret;

    /**
     * Назва хедера, з якого беремо API key.
     */
    private String headerName = "X-API-KEY";

    /**
     * Ім'я користувача, під яким аутентифікуємося через API key.
     */
    private String username = "API-KEY-USER";

    /**
     * Роль, яку отримає користувач з валідним API key.
     */
    private String role = "ROLE_API";
}
