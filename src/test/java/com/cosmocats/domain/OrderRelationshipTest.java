package com.cosmocats.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRelationshipTest {

    @Test
    void constructorAndGettersWork() {
        OffsetDateTime now = OffsetDateTime.now();
        Order order = new Order(42L, "ORD-42", "user@example.com", now);

        assertEquals(42L, order.getId());
        assertEquals("ORD-42", order.getNumber());
        assertEquals("user@example.com", order.getCustomerEmail());
        assertEquals(now, order.getCreatedAt());
        assertNotNull(order.getLines());
        assertTrue(order.getLines().isEmpty());
    }

    @Test
    void addLineSetsBidirectionalRelation() {
        Order order = new Order();
        OrderLine line = new OrderLine();

        order.addLine(line);

        assertEquals(1, order.getLines().size());
        assertTrue(order.getLines().contains(line));
        assertEquals(order, line.getOrder());

        // повторний виклик addLine не дублює елемент
        order.addLine(line);
        assertEquals(1, order.getLines().size());
    }

    @Test
    void setLinesReplacesCollectionAndUpdatesBackReference() {
        Order order = new Order();
        OrderLine line1 = new OrderLine();
        OrderLine line2 = new OrderLine();

        order.addLine(line1);
        assertEquals(order, line1.getOrder());

        List<OrderLine> newLines = new ArrayList<>();
        newLines.add(line2);

        order.setLines(newLines);

        assertEquals(1, order.getLines().size());
        assertTrue(order.getLines().contains(line2));
        assertEquals(order, line2.getOrder());
        assertNull(line1.getOrder());
    }

    @Test
    void setLinesWithNullClearsExistingLines() {
        Order order = new Order();
        OrderLine line = new OrderLine();
        order.addLine(line);

        order.setLines(null);

        assertTrue(order.getLines().isEmpty());
        assertNull(line.getOrder());
    }

    @Test
    void removeLineDetachesBothSidesAndIgnoresNull() {
        Order order = new Order();
        OrderLine line = new OrderLine();
        order.addLine(line);

        order.removeLine(line);

        assertTrue(order.getLines().isEmpty());
        assertNull(line.getOrder());

        // гілка, де параметр null
        order.removeLine(null);
        assertTrue(order.getLines().isEmpty());
    }

    @Test
    void settersUpdateSimpleFields() {
        Order order = new Order();
        OffsetDateTime created = OffsetDateTime.now();

        order.setId(7L);
        order.setCustomerEmail("test@example.com");
        order.setCreatedAt(created);

        assertEquals(7L, order.getId());
        assertEquals("test@example.com", order.getCustomerEmail());
        assertEquals(created, order.getCreatedAt());
    }
}
