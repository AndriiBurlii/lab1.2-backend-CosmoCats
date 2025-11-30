package com.cosmocats.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "order_lines",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_order_line_order_product",
                columnNames = { "order_id", "product_id" }
        )
)
public class OrderLine {

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

    public OrderLine() {
    }

    public OrderLine(Long id, Product product, int qty, BigDecimal priceAtPurchase) {
        this.id = id;
        this.product = product;
        this.qty = qty;
        this.priceAtPurchase = priceAtPurchase;
    }

    // --------- getters ---------

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

    // --------- setters ---------

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ВАЖЛИВО: тримаємо в порядку обидві сторони зв’язку.
     * Якщо тест викликає line.setOrder(order) – воно автоматично
     * додасть лінію в order.getLines().
     */
    public void setOrder(Order order) {
        // відчепитися від старого order
        if (this.order != null && this.order != order) {
            this.order.getLines().remove(this);
        }

        this.order = order;

        if (order != null && !order.getLines().contains(this)) {
            order.getLines().add(this);
        }
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
