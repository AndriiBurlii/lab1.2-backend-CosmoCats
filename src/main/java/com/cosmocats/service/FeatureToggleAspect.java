package com.cosmocats.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class FeatureToggleAspect {

    private static final Logger log = LoggerFactory.getLogger(FeatureToggleAspect.class);

    private final FeatureToggleService toggles;

    public FeatureToggleAspect(FeatureToggleService toggles) {
        this.toggles = toggles;
    }

    @Around("@annotation(flag)")
    public Object aroundFeatureMethod(ProceedingJoinPoint pjp, FeatureFlag flag) throws Throwable {
        String name = flag.value();
        boolean on = toggles.isEnabled(name);

        log.info("Feature '{}' enabled={}", name, on);

        if (on) {
            return pjp.proceed();
        }

        // exception сам формує текст повідомлення
        throw new FeatureNotAvailableException(name);
    }
}
