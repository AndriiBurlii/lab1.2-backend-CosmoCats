
package com.cosmocats.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "name must not be blank")
    private String name;

    @Positive(message = "price must be > 0")
    private BigDecimal price;

    @NotBlank(message = "category must not be blank")
    private String category;

    public ProductRequest() {}

    public ProductRequest(String name, BigDecimal price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
