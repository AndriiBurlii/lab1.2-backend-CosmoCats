package com.cosmocats.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {

    /**
     * Ключ — назва фічі (наприклад, "cosmoCats").
     * Значення — об'єкт із прапорцем enabled.
     */
    private final Map<String, FeatureProps> toggles;

    // Spring-boot 3: constructor binding за замовчуванням
    public FeatureToggleConfig(Map<String, FeatureProps> toggles) {
        this.toggles = Map.copyOf(toggles); // робимо копію, щоб ніхто не міняв ззовні
    }

    public Map<String, FeatureProps> getToggles() {
        return toggles;
    }

    /** Зручний метод перевірки */
    public boolean isEnabled(String name) {
        FeatureProps fp = toggles.get(name);
        return fp != null && fp.isEnabled();
    }

    /** Внутрішній клас із boolean-полем */
    public static class FeatureProps {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
