package com.cosmocats.service;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FeatureToggleConfigTest {

    @Test
    void isEnabledReturnsFalseWhenNoEntry() {
        FeatureToggleConfig cfg = new FeatureToggleConfig();
        assertFalse(cfg.isEnabled("cosmoCats"));
    }

    @Test
    void isEnabledUsesFeaturePropsFlag() {
        FeatureToggleConfig cfg = new FeatureToggleConfig();

        FeatureToggleConfig.FeatureProps props = new FeatureToggleConfig.FeatureProps();
        // за замовчуванням false
        assertFalse(props.isEnabled());

        props.setEnabled(true);

        Map<String, FeatureToggleConfig.FeatureProps> map = new HashMap<>();
        map.put("cosmoCats", props);

        cfg.setToggles(map);

        assertSame(map, cfg.getToggles());
        assertTrue(cfg.isEnabled("cosmoCats"));
        assertFalse(cfg.isEnabled("otherFeature"));
    }
}
