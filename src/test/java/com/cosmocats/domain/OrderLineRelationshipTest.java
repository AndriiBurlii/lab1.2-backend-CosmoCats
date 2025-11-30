package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineRelationshipTest {

    @Test
    void setOrderMovesLineBetweenOrders() {
        Order order1 = new Order();
        Order order2 = new Order();
        OrderLine line = new OrderLine();

        line.setOrder(order1);

        assertEquals(order1, line.getOrder());
        assertTrue(order1.getLines().contains(line));

        // переносимо лінію в інше замовлення
        line.setOrder(order2);

        assertEquals(order2, line.getOrder());
        assertFalse(order1.getLines().contains(line));
        assertTrue(order2.getLines().contains(line));

        // відчіпляємо від будь-якого замовлення
        line.setOrder(null);
        assertNull(line.getOrder());
        assertFalse(order2.getLines().contains(line));
    }

    @Test
    void basicSettersAndGettersWork() {
        Product product = new Product();
        product.setId(100L);

        OrderLine line = new OrderLine();
        line.setId(1L);
        line.setProduct(product);
        line.setQty(3);
        line.setPriceAtPurchase(new BigDecimal("5.50"));

        assertEquals(1L, line.getId());
        assertEquals(product, line.getProduct());
        assertEquals(3, line.getQty());
        assertEquals(new BigDecimal("5.50"), line.getPriceAtPurchase());
    }
}
