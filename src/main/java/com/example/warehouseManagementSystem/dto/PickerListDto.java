package com.example.warehouseManagementSystem.dto;

import com.example.domain.OrderItem;
import com.example.dto.OrderItemDto;

import java.util.List;

public class PickerListDto {
    private String userName;
    private String phone;
    private String comment;
    private List<OrderItemDto> orderItems;


    public PickerListDto(String userName, String phone, String comment, List<OrderItemDto> orderItems) {
        this.userName = userName;
        this.phone = phone;
        this.comment = comment;
        this.orderItems = orderItems;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}
