package com.example.dto;

import com.example.domain.Order;
import com.example.domain.OrderItem;

import java.util.List;

public class UserProfileDto {

    private String name;

    private String phone;

    private String address;

    private List<OrderResponseDto> orders;

    public UserProfileDto(String name, String phone, String address, List<OrderResponseDto> orders) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderResponseDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}


