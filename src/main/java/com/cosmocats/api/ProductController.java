package com.cosmocats.api;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;
import com.cosmocats.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // <--- ДОДАЙ ЦЕЙ ІМПОРТ
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // --- ТІЛЬКИ АДМІН МОЖЕ СТВОРЮВАТИ ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    // --- ЧИТАТИ МОЖУТЬ ВСІ АВТОРИЗОВАНІ (ЮЗЕРИ ТЕЖ) ---
    // (Анотація не обов'язкова, якщо в SecurityConfig стоїть .anyRequest().authenticated())
    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @GetMapping
    public List<ProductResponse> list() {
        return service.list();
    }

    // --- ТІЛЬКИ АДМІН МОЖЕ РЕДАГУВАТИ ---
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable long id,
                                  @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    // --- ТІЛЬКИ АДМІН МОЖЕ ВИДАЛЯТИ ---
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
