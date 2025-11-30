package com.cosmocats.domain;

// якщо пакет інший, наприклад org.example.cosmocats.domain, поміняй тут і в імпортах

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Імпорти сутностей – підлаштуй під свій пакет, якщо потрібно
import com.cosmocats.domain.Category;
import com.cosmocats.domain.Product;
import com.cosmocats.domain.Order;
import com.cosmocats.domain.OrderLine;

class DomainModelTest {

    @Test
    void product_builder_and_getters_work() {
        Category category = Category.builder()
                .id(1L)
                .code("space-food")
                .title("Space Food")
                .build();

        Product product = Product.builder()
                .id(10L)
                .name("Space Pizza")
                .price(new BigDecimal("9.99"))
                .category(category)
                .build();

        assertEquals(10L, product.getId());
        assertEquals("Space Pizza", product.getName());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals(category, product.getCategory());
    }

    @Test
    void order_with_lines_builds_correctly() {
        Product product1 = Product.builder()
                .id(1L)
                .name("Space Pizza")
                .price(new BigDecimal("9.99"))
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Cosmo Burger")
                .price(new BigDecimal("7.50"))
                .build();

        OrderLine line1 = OrderLine.builder()
                .id(100L)
                .product(product1)
                .qty(2)
                .priceAtPurchase(new BigDecimal("9.99"))
                .build();

        OrderLine line2 = OrderLine.builder()
                .id(101L)
                .product(product2)
                .qty(1)
                .priceAtPurchase(new BigDecimal("7.50"))
                .build();

        Order order = Order.builder()
                .id(50L)
                .orderNumber("ORD-1")
                .customerEmail("cat@example.com")
                .createdAt(Instant.now())
                .lines(List.of(line1, line2))
                .build();

        assertEquals("ORD-1", order.getOrderNumber());
        assertEquals("cat@example.com", order.getCustomerEmail());
        assertEquals(2, order.getLines().size());
        assertEquals(product1, order.getLines().get(0).getProduct());
        assertEquals(2, order.getLines().get(0).getQty());
    }
}
