package com.cosmocats.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureToggleAspectTest {

    @Mock
    private FeatureToggleService toggleService;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private FeatureFlag featureFlag;

    @Test
    void proceeds_whenFeatureEnabled() throws Throwable {
        when(featureFlag.value()).thenReturn("cosmoCats");
        when(toggleService.isEnabled("cosmoCats")).thenReturn(true);
        when(pjp.proceed()).thenReturn("OK");

        FeatureToggleAspect aspect = new FeatureToggleAspect(toggleService);
        Object result = aspect.aroundFeatureMethod(pjp, featureFlag);

        assertEquals("OK", result);
        verify(pjp).proceed();
    }

    @Test
    void throwsException_whenFeatureDisabled() throws Throwable {
        when(featureFlag.value()).thenReturn("kittyProducts");
        when(toggleService.isEnabled("kittyProducts")).thenReturn(false);

        FeatureToggleAspect aspect = new FeatureToggleAspect(toggleService);

        assertThrows(
                FeatureNotAvailableException.class,
                () -> aspect.aroundFeatureMethod(pjp, featureFlag)
        );
        verify(pjp, never()).proceed();
    }
}
