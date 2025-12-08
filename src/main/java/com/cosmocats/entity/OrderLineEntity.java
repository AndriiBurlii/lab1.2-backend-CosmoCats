package com.cosmocats.repository.entity;

import com.cosmocats.domain.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_lines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_line_order_product",
                columnNames = {"order_id", "product_id"}
        )
)
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "qty", nullable = false)
    private int qty;

    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;

    // гетери/сетери як звичайно
    // ...
}
