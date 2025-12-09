package com.cosmocats.entity;

import com.cosmocats.domain.Order;
import com.cosmocats.domain.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "OrderLine")
@Table(
        name = "order_lines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_line_order_product",
                columnNames = { "order_id", "product_id" }
        )
)
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_seq")
    @SequenceGenerator(
            name = "order_line_seq",
            sequenceName = "order_line_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_line_order")
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_line_product")
    )
    private Product product;

    @Column(nullable = false)
    private int qty;

    @Column(name = "price_at_purchase", nullable = false, precision = 19, scale = 2)
    private BigDecimal priceAtPurchase;

    public OrderLineEntity() {
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQty() {
        return qty;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}

