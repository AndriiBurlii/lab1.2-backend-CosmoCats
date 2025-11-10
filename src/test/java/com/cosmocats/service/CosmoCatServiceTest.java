
package com.cosmocats.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

class CosmoCatServiceTestBase {
    @Autowired
    protected CosmoCatService service;
}

@SpringBootTest
@TestPropertySource(properties = {
        "feature.cosmoCats.enabled=true",
        "feature.kittyProducts.enabled=false"
})
class CosmoCatServiceEnabledTest extends CosmoCatServiceTestBase {
    @Test
    void getCosmoCats_returns_when_enabled() {
        assertFalse(service.getCosmoCats().isEmpty());
    }
}

@SpringBootTest
@TestPropertySource(properties = {
        "feature.cosmoCats.enabled=false",
        "feature.kittyProducts.enabled=false"
})
class CosmoCatServiceDisabledTest extends CosmoCatServiceTestBase {
    @Test
    void getCosmoCats_throws_when_disabled() {
        var ex = assertThrows(FeatureNotAvailableException.class, () -> service.getCosmoCats());
        assertTrue(ex.getMessage().contains("cosmoCats"));
    }
}
