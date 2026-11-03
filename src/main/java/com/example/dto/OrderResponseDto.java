package com.example.dto;

import com.example.domain.Order;
import com.example.domain.OrderStatus;


import java.math.BigDecimal;
import java.util.List;

public class OrderResponseDto {
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private List<OrderItemDto> orderItems;


    public OrderResponseDto(Order order) {
        this.orderNumber = order.getNumberOrder();
        this.orderStatus = order.getOrderStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems() != null ?
                order.getOrderItems().stream()
                        .map(OrderItemDto::new)
                        .toList() : List.of();
    }


    public OrderResponseDto(String orderNumber, OrderStatus orderStatus, BigDecimal totalPrice, List<OrderItemDto> orderItems) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}
