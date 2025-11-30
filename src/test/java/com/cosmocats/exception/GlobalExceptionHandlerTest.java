package com.cosmocats.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/api/test");
    }

    @Test
    void handleResourceNotFound_returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Product", 42L);

        ResponseEntity<?> response = handler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleUniqueValueAlreadyExists_returns409() {
        UniqueValueAlreadyExistsException ex =
                new UniqueValueAlreadyExistsException("products", "name");

        ResponseEntity<?> response = handler.handleUniqueValueAlreadyExists(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleNoResourceFound_returns404() {
        NoResourceFoundException ex = new NoResourceFoundException("GET", "/wrong-url");

        ResponseEntity<?> response = handler.handleNoResourceFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleTypeMismatch_returns400() throws NoSuchMethodException {
        Method m = SampleClass.class.getMethod("sampleMethod", Long.class);
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("abc", Long.class, "id", null, null);

        ResponseEntity<?> response = handler.handleTypeMismatch(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGenericException_returns500() {
        RuntimeException ex = new RuntimeException("boom");

        ResponseEntity<?> response = handler.handleOther(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Допоміжний клас для рефлексії у type-mismatch тесті
    static class SampleClass {
        public void sampleMethod(Long id) {
        }
    }
}
