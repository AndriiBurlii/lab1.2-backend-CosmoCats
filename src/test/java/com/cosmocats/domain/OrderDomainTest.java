package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Базовий доменний тест для Order.
 * Не прив’язуємося до конкретних полів (orderNumber, category тощо),
 * лише перевіряємо, що клас існує, створюється і коректно поводиться як об’єкт.
 */
class OrderDomainTest {

    @Test
    void orderClassCanBeInstantiated() {
        Order order = new Order();
        assertNotNull(order);
        // toString ніколи не повинен повертати null
        assertNotNull(order.toString());
    }
}
