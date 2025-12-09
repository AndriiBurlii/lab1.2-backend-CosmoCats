package com.cosmocats.service;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;
import com.cosmocats.domain.Product;
import com.cosmocats.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository repo;

    public DefaultProductService(ProductRepository repo) {
        this.repo = repo;
    }

    private ProductResponse toDto(Product p) {
        Objects.requireNonNull(p, "Product is null");
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCategory()
        );
    }

    @Override
    @FeatureFlag("cosmoCats")
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest r) {
        boolean nameExists = repo.existsByNameIgnoreCase(r.getName());
        if (nameExists) {
            throw new ProductAlreadyExistsException(r.getName());
        }

        Product saved = repo.save(new Product(null, r.getName(), r.getPrice(), r.getCategory()));

        if (saved == null) {
            throw new ProductAlreadyExistsException(r.getName());
        }

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return toDto(p);
    }

    @Override
    @Transactional
    public ProductResponse update(long id, ProductRequest r) {
        // ⚠️ Тут спеціально залишаємо IllegalArgumentException,
        // бо цього чекає ProductServiceUnitTest.update_whenNotFound_throws
        Product p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found id=" + id));

        p.setName(r.getName());
        p.setPrice(r.getPrice());
        p.setCategory(r.getCategory());

        return toDto(repo.save(p));
    }

    @Override
    @Transactional
    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repo.deleteById(id);
    }
}
