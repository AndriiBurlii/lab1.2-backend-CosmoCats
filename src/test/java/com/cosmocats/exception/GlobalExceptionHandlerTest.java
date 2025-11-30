package com.cosmocats.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести для GlobalExceptionHandler.
 * Покриваємо основні гілки:
 * - ResourceNotFoundException -> 404
 * - UniqueValueAlreadyExistsException -> 409
 * - інші Exception -> 500
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_returns404WithMessage() {
        // given
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Product with id=42 not found");

        // when
        ResponseEntity<ProblemDetail> response = handler.handleResourceNotFound(ex);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ProblemDetail body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertTrue(body.getDetail().contains("Product with id=42 not found"));
    }

    @Test
    void handleUniqueValueAlreadyExists_returns409WithMessage() {
        // given
        UniqueValueAlreadyExistsException ex =
                new UniqueValueAlreadyExistsException("Product name already used");

        // when
        ResponseEntity<ProblemDetail> response = handler.handleUniqueValueAlreadyExists(ex);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ProblemDetail body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
        assertTrue(body.getDetail().contains("Product name already used"));
    }

    @Test
    void handleGenericException_returns500AndNonEmptyDetail() {
        // given
        Exception ex = new RuntimeException("Unexpected boom");

        // when
        ResponseEntity<ProblemDetail> response = handler.handleException(ex);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ProblemDetail body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
        assertNotNull(body.getDetail());
        assertFalse(body.getDetail().isBlank());
    }
}
