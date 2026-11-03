package com.example.controller;

import com.example.domain.User;
import com.example.dto.CartItemDto;
import com.example.dto.CartResponseDto;
import com.example.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public CartResponseDto getUser(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        return cartService.getAllProducts(user.getId());
    }



    @PostMapping("/addItem")
    // ДОБАВИЛИ @RequestBody, чтобы Spring читал JSON из Postman
    public ResponseEntity<String> addItem(HttpSession session, @RequestBody CartItemDto cartItemDto) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
            }
            cartService.linkSessionCardAndItem(user.getId(), cartItemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Выбранный товар успешно добавлен в корзину");
        }
        catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); // Для "Товар не найден" лучше 404 (NOT_FOUND)
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера: " + exception.getMessage());
        }
    }


    @PostMapping("/addItems")
    // ДОБАВИЛИ @RequestBody, чтобы Spring читал JSON из Postman
    public ResponseEntity<String> addItems(HttpSession session, @RequestBody List<CartItemDto> cartItemDtos) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
            }
            cartService.linkSessionCardAndMultipleItems(user.getId(), cartItemDtos);
            return ResponseEntity.status(HttpStatus.CREATED).body("Выбранные товары успешно добавлены в корзину");
        }
        catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); // Для "Товар не найден" лучше 404 (NOT_FOUND)
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера: " + exception.getMessage());
        }
    }


    @PutMapping("/changeQuantity")
    public ResponseEntity<String> changeUserCart(@RequestBody CartItemDto cartItemDto, HttpSession httpSession) {
        try {
            User user = (User) httpSession.getAttribute("currentUser");
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
            }
            cartService.changeProductQuantityInUserCart(cartItemDto, user.getId());
            return ResponseEntity.ok("Количество товара в корзине успешно изменено");
        } catch (ResponseStatusException exception) {
            // Перехватываем нашу 401 ошибку и возвращаем её статус и текст (без "Ошибка сервера")
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); // Для "Товар не найден" лучше 404 (NOT_FOUND)
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера: " + exception.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItemFromUserCart(@PathVariable Long id, HttpSession session) {
        try {
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
            }
            cartService.deleteItemFromUserCart(id, user.getId());
            return ResponseEntity.ok("Удаляемый товар в корзине успешно удален");
        } catch (ResponseStatusException exception) {
            // Перехватываем нашу 401 ошибку и возвращаем её статус и текст (без "Ошибка сервера")
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); // Для "Товар не найден" лучше 404 (NOT_FOUND)
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера: " + exception.getMessage());
        }
    }
}
