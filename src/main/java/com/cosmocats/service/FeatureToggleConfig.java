package com.cosmocats.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {

    /**
     * Ключ — назва фічі (napр. "cosmoCats").
     * Значення — об'єкт із прапорцем enabled.
     */
    private Map<String, FeatureProps> toggles = new HashMap<>();

    public Map<String, FeatureProps> getToggles() {
        return toggles;
    }

    public void setToggles(Map<String, FeatureProps> toggles) {
        this.toggles = toggles;
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
