package com.cosmocats.api;

import com.cosmocats.domain.Category;
import com.cosmocats.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {

    @Test
    void category_gettersAndSetters_work() {
        Category category = new Category();

        // припускаємо стандартні сетери та гетери
        category.setId(1L);
        category.setCode("FOOD");
        category.setTitle("Food");

        assertEquals(1L, category.getId());
        assertEquals("FOOD", category.getCode());
        assertEquals("Food", category.getTitle());
    }

    @Test
    void product_gettersAndSetters_work() {
        Category category = new Category();
        category.setId(1L);
        category.setCode("FOOD");
        category.setTitle("Food");

        Product product = new Product();
        product.setId(10L);
        product.setName("Space Pizza");
        product.setPrice(new BigDecimal("9.99"));
        product.setCategory(category);

        assertEquals(10L, product.getId());
        assertEquals("Space Pizza", product.getName());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals(category, product.getCategory());
    }
}
