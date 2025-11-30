package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainTest {

    @Test
    void createOrderWithLines_andUseGettersSetters() {
        Category category = new Category();
        category.setId(1L);
        category.setCode("FOOD");
        category.setTitle("Space food");

        Product product1 = new Product();
        product1.setId(10L);
        product1.setName("Space Pizza");
        product1.setPrice(new BigDecimal("10.50"));
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setId(11L);
        product2.setName("Galaxy Burger");
        product2.setPrice(new BigDecimal("7.30"));
        product2.setCategory(category);

        Order order = new Order();
        order.setId(100L);
        order.setOrderNumber("ORD-001");
        order.setCustomerEmail("cat@cosmo.cats");
        order.setCreatedAt(OffsetDateTime.now());

        OrderLine line1 = new OrderLine();
        line1.setId(1000L);
        line1.setOrder(order);
        line1.setProduct(product1);
        line1.setQty(2);
        line1.setPriceAtPurchase(product1.getPrice());

        OrderLine line2 = new OrderLine();
        line2.setId(1001L);
        line2.setOrder(order);
        line2.setProduct(product2);
        line2.setQty(1);
        line2.setPriceAtPurchase(product2.getPrice());

        order.getLines().add(line1);
        order.getLines().add(line2);

        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals("cat@cosmo.cats", order.getCustomerEmail());
        assertEquals(2, order.getLines().size());

        assertEquals(product1, order.getLines().get(0).getProduct());
        assertEquals(product2, order.getLines().get(1).getProduct());
    }
}
