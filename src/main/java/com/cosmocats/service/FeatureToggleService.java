package com.cosmocats.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {

    private final Environment env;

    public FeatureToggleService(Environment env) {
        this.env = env;
    }

    /**
     * Читає значення напряму з конфігів/аргументів:
     * - application.yml / properties
     * - --feature.cosmoCats.enabled=true (CLI)
     * - змінні середовища (SPRING_APPLICATION_JSON тощо)
     */
    public boolean isEnabled(String featureName) {
        // ключ у форматі feature.<name>.enabled
        String key = "feature." + featureName + ".enabled";
        // читаємо як Boolean; default=false
        Boolean val = env.getProperty(key, Boolean.class, Boolean.FALSE);
        return Boolean.TRUE.equals(val);
    }
}
