package com.cosmocats.service;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.api.dto.ProductResponse;
import com.cosmocats.domain.Product;
import com.cosmocats.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultProductServiceTest {

    private ProductRepository repository;
    private DefaultProductService service;

    @BeforeEach
    void setup() {
        repository = mock(ProductRepository.class);
        service = new DefaultProductService(repository);
    }

    @Test
    void create_ok() {
        ProductRequest req = new ProductRequest("Laser Mouse", new BigDecimal("99.99"), "gadgets");

        when(repository.existsByNameIgnoreCase("Laser Mouse")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductResponse resp = service.create(req);

        assertNotNull(resp.getId());
        assertEquals("Laser Mouse", resp.getName());
        ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(cap.capture());
        assertEquals("gadgets", cap.getValue().getCategory());
    }

    @Test
    void create_duplicateName_throws() {
        when(repository.existsByNameIgnoreCase("X")).thenReturn(true);
        ProductRequest req = new ProductRequest("X", new BigDecimal("1.00"), "c");

        ProductAlreadyExistsException ex =
                assertThrows(ProductAlreadyExistsException.class, () -> service.create(req));

        assertTrue(ex.getMessage().toLowerCase().contains("already exists"));
    }

    @Test
    void get_ok() {
        Product p = new Product(7L, "A", new BigDecimal("10.00"), "c");
        when(repository.findById(7L)).thenReturn(Optional.of(p));

        ProductResponse resp = service.get(7L);
        assertEquals("A", resp.getName());
    }

    @Test
    void get_notFound() {
        when(repository.findById(8L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.get(8L));
    }

    @Test
    void list_ok() {
        when(repository.findAll()).thenReturn(List.of(
                new Product(1L, "A", new BigDecimal("1.00"), "c"),
                new Product(2L, "B", new BigDecimal("2.00"), "c")
        ));

        List<ProductResponse> all = service.list();
        assertEquals(2, all.size());
    }

    @Test
    void update_ok() {
        Product existing = new Product(3L, "Old", new BigDecimal("1.00"), "c1");
        when(repository.findById(3L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductRequest req = new ProductRequest("New", new BigDecimal("2.50"), "c2");
        ProductResponse resp = service.update(3L, req);

        assertEquals("New", resp.getName());
        assertEquals(new BigDecimal("2.50"), resp.getPrice());
    }

    @Test
    void delete_ok() {
        when(repository.existsById(9L)).thenReturn(true);

        service.delete(9L);

        verify(repository).deleteById(9L);
    }

    @Test
    void delete_notFound() {
        when(repository.existsById(10L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> service.delete(10L));
    }
}
