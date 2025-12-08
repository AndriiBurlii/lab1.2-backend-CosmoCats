package com.cosmocats.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureToggleServiceTest {

    @Mock
    private FeatureToggleConfig config;

    private FeatureToggleService service;

    @BeforeEach
    void setUp() {
        service = new FeatureToggleService(config);
    }

    @Test
    void isEnabled_returnsTrue_whenConfigSaysTrue() {
        when(config.isEnabled("cosmoCats")).thenReturn(true);

        boolean result = service.isEnabled("cosmoCats");

        assertTrue(result);
        verify(config).isEnabled("cosmoCats");
    }

    @Test
    void isEnabled_returnsFalse_whenConfigSaysFalse() {
        when(config.isEnabled("kittyProducts")).thenReturn(false);

        boolean result = service.isEnabled("kittyProducts");

        assertFalse(result);
        verify(config).isEnabled("kittyProducts");
    }
}
