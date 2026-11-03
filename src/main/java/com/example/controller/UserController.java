package com.example.controller;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.User;
import com.example.dto.LoginDto;
import com.example.dto.OrderResponseDto;
import com.example.dto.UserDto;
import com.example.dto.UserProfileDto;
import com.example.repository.OrderRepository;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OrderRepository orderRepository;

    public UserController(UserService userService, OrderRepository orderRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public UserProfileDto getUserProfile(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        List<Order> orders = orderRepository.findByUser_Id(currentUser.getId());

        List<OrderResponseDto> userOrders = orders.stream()
                .map(OrderResponseDto::new).toList();

        List<OrderItem> userOrderItems = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .toList();
        return new UserProfileDto(
                currentUser.getName(),
                currentUser.getPhone(),
                currentUser.getAddress(),
                userOrders
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpSession session) {

        User user = userService.getUser(loginDto);

        session.setAttribute("currentUser", user);
        return ResponseEntity.ok("Успешный вход");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {

        userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован!");


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {

        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь успешно удален!");
    }
}
