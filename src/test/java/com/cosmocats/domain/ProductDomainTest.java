package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDomainTest {

    @Test
    void fullPropertiesAndCategoryEntity() {
        Category category = new Category(1L, "FOOD", "Space food");

        Product product = new Product();
        product.setId(1L);
        product.setName("Burger");
        product.setPrice(new BigDecimal("10.00"));
        product.setCategory("FOOD");
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

        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Burger");
        p1.setPrice(new BigDecimal("10.00"));
        p1.setCategory("FOOD");
        p1.setCategoryEntity(category);

        Product p2 = new Product();
        p2.setId(1L);
        p2.setName("Burger");
        p2.setPrice(new BigDecimal("10.00"));
        p2.setCategory("FOOD");
        p2.setCategoryEntity(category);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setName("Other");
        assertNotEquals(p1, p2);
    }
}
