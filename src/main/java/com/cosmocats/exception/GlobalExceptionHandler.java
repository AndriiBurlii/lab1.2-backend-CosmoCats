package com.cosmocats.exception;

import com.cosmocats.service.FeatureNotAvailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- helpers
    private Map<String, Object> body(HttpStatus status, String message, HttpServletRequest req) {
        Map<String, Object> m = new HashMap<>();
        m.put("timestamp", OffsetDateTime.now().toString());
        m.put("status", status.value());
        m.put("error", status.getReasonPhrase());
        m.put("message", message);
        m.put("path", req.getRequestURI());
        return m;
    }

    // 1) Feature toggle OFF -> 403 (можеш змінити на SERVICE_UNAVAILABLE якщо хочеш 503)
    @ExceptionHandler(FeatureNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleFeatureToggle(
            FeatureNotAvailableException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.FORBIDDEN; // або HttpStatus.SERVICE_UNAVAILABLE
        return ResponseEntity.status(status).body(body(status, ex.getMessage(), req));
    }

    // 2) Некоректні аргументи бізнес-логіки -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(body(status, ex.getMessage(), req));
    }

    // 3) Bean Validation @Valid (тіло запиту) -> 400 + список помилок полів
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> b = body(status, "Validation failed", req);
        List<Map<String, Object>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> {
                    Map<String, Object> e = new HashMap<>();
                    e.put("field", fe.getField());
                    e.put("rejectedValue", fe.getRejectedValue());
                    e.put("message", fe.getDefaultMessage());
                    return e;
                })
                .toList();
        b.put("errors", fieldErrors);
        return ResponseEntity.status(status).body(b);
    }

    // 4) Bean Validation для параметрів у рядку запиту/шляху -> 400
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(body(status, ex.getMessage(), req));
    }

    // 5) Невірні типи параметрів (наприклад, id=abc) -> 400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "Parameter '%s' has invalid value '%s'".formatted(
                ex.getName(), ex.getValue()
        );
        return ResponseEntity.status(status).body(body(status, msg, req));
    }

    // 6) Інше непередбачене -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(
            Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(body(status, ex.getMessage(), req));
    }
}
