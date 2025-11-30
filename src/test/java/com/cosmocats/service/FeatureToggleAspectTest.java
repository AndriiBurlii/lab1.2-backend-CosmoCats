package com.cosmocats.service;

import com.cosmocats.exception.FeatureNotAvailableException;
import com.cosmocats.feature.FeatureToggle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureToggleAspectTest {

    @Mock
    private FeatureToggleService featureToggleService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private FeatureToggle toggle;

    @InjectMocks
    private FeatureToggleAspect aspect;

    @Test
    void around_whenFeatureEnabled_proceeds() throws Throwable {
        // given
        when(toggle.value()).thenReturn("cosmoCats");
        when(featureToggleService.isEnabled("cosmoCats")).thenReturn(true);

        Object expected = new Object();
        when(joinPoint.proceed()).thenReturn(expected);

        // when
        Object result = aspect.around(joinPoint, toggle);

        // then
        assertSame(expected, result);
        verify(joinPoint, times(1)).proceed();
        verify(featureToggleService).isEnabled("cosmoCats");
    }

    @Test
    void around_whenFeatureDisabled_throwsFeatureNotAvailable() {
        // given
        when(toggle.value()).thenReturn("cosmoCats");
        when(featureToggleService.isEnabled("cosmoCats")).thenReturn(false);

        // when + then
        assertThrows(FeatureNotAvailableException.class,
                () -> aspect.around(joinPoint, toggle));

        verify(joinPoint, never()).proceed();
        verify(featureToggleService).isEnabled("cosmoCats");
    }
}
