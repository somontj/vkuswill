package com.example.dto;

import com.example.domain.Order;
import com.example.domain.OrderItem;

import java.math.BigDecimal;

public class OrderItemDto {
    String productName;
    BigDecimal price;
    Integer quantity;


    public OrderItemDto(OrderItem orderItem) {
        this.productName = orderItem.getProduct().getName();
        this.price = orderItem.getPriceProduct();
        this.quantity = orderItem.getQuantity();
    }

    public OrderItemDto(String productName, BigDecimal price, Integer quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
