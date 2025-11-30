package com.cosmocats.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureNotAvailableExceptionTest {

    @Test
    void messageIsPropagated() {
        FeatureNotAvailableException ex =
                new FeatureNotAvailableException("feature disabled");

        assertEquals("feature disabled", ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
}
