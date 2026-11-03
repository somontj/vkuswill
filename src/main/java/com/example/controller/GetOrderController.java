package com.example.controller;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.User;
import com.example.dto.OrderItemDto;
import com.example.dto.OrderResponseDto;
import com.example.dto.UserProfileDto;
import com.example.exception.UserNotFoundException;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class GetOrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public GetOrderController(OrderService orderService, UserRepository userRepository, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public UserProfileDto getOrder(@RequestParam String phone) {
        User user = userRepository.findUserByPhone(phone.trim().replaceAll("\\s+", " "));
        if (user == null) {
            throw new UserNotFoundException("Пользователь с таким " + phone + " не найден");
        }
        List<Order> orders = orderRepository.findByUser_Id(user.getId());

        List<OrderResponseDto> orderItems = orders.stream()
                .map(OrderResponseDto::new)
                .toList();
        List<OrderItem> userOrderItems = orders.stream()
                .flatMap(i -> i.getOrderItems().stream())
                .toList();

        return new UserProfileDto(
                user.getName(),
                user.getPhone(),
                user.getAddress(),
                orderItems);
    }
}
