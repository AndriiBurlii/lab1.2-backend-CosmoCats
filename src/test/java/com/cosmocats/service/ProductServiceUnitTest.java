package com.cosmocats.service;

import com.cosmocats.api.dto.ProductRequest;
import com.cosmocats.domain.Product;
import com.cosmocats.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    DefaultProductService service;

    @Test
    void list_ok() {
        when(repository.findAll()).thenReturn(List.of(new Product(1L, "A", new BigDecimal("1.00"), "c")));
        assertThat(service.list()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void update_whenNotFound_throws() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.update(999L, new ProductRequest()));
    }
}
