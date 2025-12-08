package com.cosmocats.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderLine {

    private Long id;
    private Order order;
    private Product product;
    private int qty;
    private BigDecimal priceAtPurchase;

    public OrderLine() {
    }

    public OrderLine(Long id, Order order, Product product, int qty, BigDecimal priceAtPurchase) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.qty = qty;
        this.priceAtPurchase = priceAtPurchase;
    }

    // --- relationships ---

    public void setOrder(Order newOrder) {
        if (this.order == newOrder) {
            return;
        }
        // відв’язуємося від старого ордера
        if (this.order != null) {
            this.order.internalRemoveLine(this);
        }
        this.order = newOrder;
        // прив’язуємося до нового
        if (newOrder != null) {
            newOrder.internalAddLine(this);
        }
    }

    // --- getters / setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    // equals / hashCode тільки по id (або по тому, як ти робиш у Product)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLine)) return false;
        OrderLine that = (OrderLine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
