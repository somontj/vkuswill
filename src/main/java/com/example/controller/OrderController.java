package com.example.controller;

import com.example.domain.User;
import com.example.dto.CreateOrderDto;
import com.example.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(HttpSession session, @RequestBody CreateOrderDto createOrderDto) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
            }
            orderService.createOrder(user.getId(), createOrderDto.getAddress(), createOrderDto.getComment());
            return ResponseEntity.status(HttpStatus.CREATED).body("Заказ успешно создан!");
        }
        catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутреняя ошибка сервера" + exception.getMessage());
        }
    }
}