package com.cosmocats.api.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseTest {

    @Test
    void noArgsConstructorAndSettersWork() {
        ProductResponse dto = new ProductResponse();

        dto.setId(1L);
        dto.setName("Burger");
        dto.setPrice(new BigDecimal("12.34"));
        dto.setCategory("FOOD");

        assertEquals(1L, dto.getId());
        assertEquals("Burger", dto.getName());
        assertEquals(new BigDecimal("12.34"), dto.getPrice());
        assertEquals("FOOD", dto.getCategory());
    }

    @Test
    void allArgsConstructorSetsFields() {
        ProductResponse dto =
                new ProductResponse(2L, "Cola", new BigDecimal("3.50"), "DRINK");

        assertEquals(2L, dto.getId());
        assertEquals("Cola", dto.getName());
        assertEquals(new BigDecimal("3.50"), dto.getPrice());
        assertEquals("DRINK", dto.getCategory());
    }
}
