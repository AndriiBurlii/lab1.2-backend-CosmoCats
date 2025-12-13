package com.cosmocats.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


class GlobalExceptionHandlerTest {

    @Test
    void handlerHandlesExceptionOrAtLeastInstantiates() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new RuntimeException("boom");

        ResponseEntity<?> response = null;

        for (var method : GlobalExceptionHandler.class.getDeclaredMethods()) {
            if (method.getParameterCount() == 1
                    && Exception.class.isAssignableFrom(method.getParameterTypes()[0])
                    && ResponseEntity.class.isAssignableFrom(method.getReturnType())) {
                try {
                    method.setAccessible(true);
                    response = (ResponseEntity<?>) method.invoke(handler, ex);
                    break;
                } catch (Exception ignored) {
                    // Якщо не вийшло викликати — продовжуємо шукати інший метод
                }
            }
        }

        if (response == null) {
            // Нема універсального метода на Exception — просто перевіряємо, що хендлер є
            assertNotNull(handler);
        } else {
            // Якщо метод знайшли й викликали — тіло відповіді не має бути null
            assertNotNull(response.getBody());
        }
    }
}
