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
        // старе замовлення, до якого "нормально" прикріплена лінія
        Order oldOrder = new Order();
        OrderLine existingLine = new OrderLine();
        oldOrder.addLine(existingLine); // existingLine.order = oldOrder, oldOrder.lines = [existingLine]

        // нове замовлення, у якого в списку лежить та сама лінія,
        // але її order все ще вказує на oldOrder
        Order order = new Order();
        order.getLines().add(existingLine); // order.lines = [existingLine], existingLine.order = oldOrder

        // нові лінії, які мають прийти у setLines(...)
        OrderLine line1 = new OrderLine();
        OrderLine line2 = new OrderLine();
        List<OrderLine> newLines = new ArrayList<>();
        newLines.add(line1);
        newLines.add(line2);

        // виклик методу, який ми тестуємо
        order.setLines(newLines);

        // стара лінія відчеплена від oldOrder і від будь-якого order
        assertFalse(oldOrder.getLines().contains(existingLine));
        assertNull(existingLine.getOrder());

        // нові лінії прив'язані до нового order
        assertEquals(2, order.getLines().size());
        assertTrue(order.getLines().contains(line1));
        assertTrue(order.getLines().contains(line2));
        assertEquals(order, line1.getOrder());
        assertEquals(order, line2.getOrder());
    }

    @Test
    void setLinesWithNullClearsExistingLines() {
        // старе замовлення з нормально прикріпленою лінією
        Order oldOrder = new Order();
        OrderLine existingLine = new OrderLine();
        oldOrder.addLine(existingLine); // existingLine.order = oldOrder

        // нове замовлення, де в списку лежить ця лінія,
        // але її order все ще oldOrder
        Order order = new Order();
        order.getLines().add(existingLine);

        // передаємо null у setLines
        order.setLines(null);

        // список ліній у нового order очищений
        assertTrue(order.getLines().isEmpty());

        // лінія відчеплена від старого замовлення і не має order
        assertFalse(oldOrder.getLines().contains(existingLine));
        assertNull(existingLine.getOrder());
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
