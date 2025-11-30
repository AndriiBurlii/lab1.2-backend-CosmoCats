package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDomainTest {

    @Test
    void fullPropertiesAndCategoryEntity() {
        Category category = new Category(1L, "FOOD", "Space food");
        Product product = new Product(1L, "Burger", new BigDecimal("10.00"), "FOOD");
        product.setCategoryEntity(category);

        assertEquals(1L, product.getId());
        assertEquals("Burger", product.getName());
        assertEquals(new BigDecimal("10.00"), product.getPrice());
        assertEquals("FOOD", product.getCategory());
        assertEquals(category, product.getCategoryEntity());
    }

    @Test
    void equalsAndHashCodeUseMainFields() {
        Category category = new Category(1L, "FOOD", "Space food");

        Product p1 = new Product(1L, "Burger", new BigDecimal("10.00"), "FOOD");
        p1.setCategoryEntity(category);

        Product p2 = new Product(1L, "Burger", new BigDecimal("10.00"), "FOOD");
        p2.setCategoryEntity(category);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setName("Other");
        assertNotEquals(p1, p2);
    }
}
