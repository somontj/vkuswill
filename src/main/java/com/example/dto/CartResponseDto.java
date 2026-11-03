package com.example.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDto {
    private String userName;

    private List<CartItemDto> items;

    private BigDecimal totalPrice;

    public CartResponseDto(String userName, List<CartItemDto> items, BigDecimal totalPrice) {
        this.userName = userName;
        this.items = items;
        this.totalPrice = totalPrice;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
