package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDomainTest {

    @Test
    void productsListAndSettersWork() {
        Category category = new Category();
        category.setId(1L);
        category.setCode("FOOD");
        category.setTitle("Space food");

        assertEquals(1L, category.getId());
        assertEquals("FOOD", category.getCode());
        assertEquals("Space food", category.getTitle());

        List<Product> products = new ArrayList<>();
        products.add(new Product());

        category.setProducts(products);

        assertSame(products, category.getProducts());
        assertEquals(1, category.getProducts().size());
    }
}
