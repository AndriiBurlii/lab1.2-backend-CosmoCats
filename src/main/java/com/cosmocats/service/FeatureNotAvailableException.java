package com.cosmocats.service;

public class FeatureNotAvailableException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "Feature '%s' is disabled";

    public FeatureNotAvailableException(String featureName) {
        super(MESSAGE_PATTERN.formatted(featureName));
    }
}
