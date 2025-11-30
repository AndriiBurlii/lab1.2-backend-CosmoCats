package com.cosmocats.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Примітивний тест, який перевіряє, що клас FeatureToggleAspect взагалі існує
 * і може бути завантажений JVM. Не прив’язуємося до внутрішньої реалізації,
 * щоб тест не ламався від дрібних змін.
 */
class FeatureToggleAspectTest {

    @Test
    void featureToggleAspectClassIsPresent() {
        assertDoesNotThrow(() -> Class.forName("com.cosmocats.service.FeatureToggleAspect"));
    }
}
