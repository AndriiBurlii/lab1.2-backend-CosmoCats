package com.cosmocats.service;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;
import com.cosmocats.domain.Product;
import com.cosmocats.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Логіка CRUD для продуктів.
 * ВАЖЛИВО: метод list() захищений фіче-флагом "cosmoCats".
 */
@Service
@Transactional
public class DefaultProductService implements ProductService {

    private final ProductRepository repository;

    public DefaultProductService(ProductRepository repository) {
        this.repository = repository;
    }

    private static ProductResponse toDto(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCategory()
        );
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product p = new Product(
                null,
                request.getName(),
                request.getPrice(),
                request.getCategory()
        );
        return toDto(repository.save(p));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(long id) {
        Product p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        return toDto(p);
    }
    @Override
    @FeatureFlag("cosmoCats")
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repository.findAll()
                .stream()
                .map(DefaultProductService::toDto)
                .toList();
    }

    @Override
    public ProductResponse update(long id, ProductRequest request) {
        Product p = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        p.setName(request.getName());
        p.setPrice(request.getPrice());
        p.setCategory(request.getCategory());
        return toDto(repository.save(p));
    }

    @Override
    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("product not found");
        }
        repository.deleteById(id);
    }
}