package com.cosmocats.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_order_number", columnNames = "order_number"),
                @UniqueConstraint(name = "uk_order_customer_email", columnNames = "customer_email")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(
            name = "order_seq",
            sequenceName = "order_seq",
            allocationSize = 1
    )
    private Long id;

    @NaturalId
    @Column(name = "order_number", nullable = false, length = 64, updatable = false)
    private String number;

    @Column(name = "customer_email", nullable = false, length = 255)
    private String customerEmail;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderLine> lines = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, String number, String customerEmail, OffsetDateTime createdAt) {
        this.id = id;
        this.number = number;
        this.customerEmail = customerEmail;
        this.createdAt = createdAt;
    }

    // --------- getters ---------

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    // --------- setters ---------

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * ВАЖЛИВО: правильно оновлюємо бідірекційний зв'язок.
     * Все, що приходить у setLines, прив'язуємо через addLine(),
     * щоб у кожної OrderLine був виставлений order.
     */
    public void setLines(List<OrderLine> lines) {
        // прибираємо старі зв'язки
        this.lines.forEach(line -> line.setOrder(null));
        this.lines.clear();

        if (lines != null) {
            for (OrderLine line : lines) {
                addLine(line);
            }
        }
    }

    // --------- helper-методи для керування лініями ---------

    public void addLine(OrderLine line) {
        if (line == null) {
            return;
        }
        // щоб не дублювати
        if (!this.lines.contains(line)) {
            this.lines.add(line);
        }
        // виставляємо зворотній зв'язок
        if (line.getOrder() != this) {
            line.setOrder(this);
        }
    }

    public void removeLine(OrderLine line) {
        if (line == null) {
            return;
        }
        this.lines.remove(line);
        if (line.getOrder() == this) {
            line.setOrder(null);
        }
    }
}
