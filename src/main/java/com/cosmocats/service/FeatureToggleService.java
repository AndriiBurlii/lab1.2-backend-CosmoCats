package com.cosmocats.service;

import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {

    private final FeatureToggleConfig config;

    public FeatureToggleService(FeatureToggleConfig config) {
        this.config = config;
    }

    public boolean isEnabled(String featureName) {
        return config.isEnabled(featureName);
    }
}
