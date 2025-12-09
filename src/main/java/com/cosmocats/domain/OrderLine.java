package com.cosmocats.domain;

import java.math.BigDecimal;

public class OrderLine {

    private Long id;
    private Order order;
    private Product product;
    private int qty;
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

    public void setOrder(Order order) {
        if (this.order == order) {
            return;
        }

        if (this.order != null) {
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
