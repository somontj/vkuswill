package com.example.service;

import com.example.domain.Cart;
import com.example.domain.CartItem;
import com.example.domain.Product;
import com.example.domain.User;
import com.example.dto.CartItemDto;
import com.example.dto.CartResponseDto;
import com.example.repository.CartItemRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }


    public CartResponseDto getAllProducts(Long id) {
        List<CartItem> cartItems = cartItemRepository.findCartItemByUserId(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));

        List<CartItemDto> itemDtos = cartItems.stream()
                .map(item -> new CartItemDto(
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getProduct().getName()
                ))
                .toList();

        BigDecimal totalPrice = itemDtos.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDto(user.getName() + " " + user.getSurname(), itemDtos, totalPrice);
    }


    @Transactional
    public void linkSessionCardAndItem(Long id, CartItemDto cartItemDto) {


        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден в БД!"));

        
        if (user.getCart() == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            user.setCart(newCart);
        }

        Cart cart = user.getCart();

        
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Добавляемый товар не найден!"));

        boolean itemExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + cartItemDto.getQuantity());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDto.getQuantity());
            cart.addItem(newItem);
        }



    }











































    @Transactional
    public void linkSessionCardAndMultipleItems(Long id, List<CartItemDto> cartItemDtos) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден в БД!"));

        if (user.getCart() == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            user.setCart(newCart);
        }

        Cart cart = user.getCart();

        
        List<Long> productIds = cartItemDtos.stream()
                .map(CartItemDto::getProductId)
                .toList();

        
        List<Product> products = productRepository.findAllById(productIds);

        
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        
        for (CartItemDto itemDto : cartItemDtos) {
            Product product = productMap.get(itemDto.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Добавляемый товар с ID " + itemDto.getProductId() + " не найден!");
            }

            boolean itemExists = false;
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(product.getId())) {
                    item.setQuantity(item.getQuantity() + itemDto.getQuantity());
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setQuantity(itemDto.getQuantity());
                cart.addItem(newItem);
            }
        }
    }



    @Transactional
    public void changeProductQuantityInUserCart(CartItemDto cartItemDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));





        
        CartItem cartItem = user.getCart().getItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartItemDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Товар в корзине не найден"));

        Product product = cartItem.getProduct();

        if (cartItemDto.getQuantity() == 0) {
            
            user.getCart().getItems().remove(cartItem);

        } else if (cartItemDto.getQuantity() > 0 && cartItemDto.getQuantity() <= product.getStockQuantity()) {
            cartItem.setQuantity(cartItemDto.getQuantity());
        } else {
            throw new IllegalArgumentException("К сожалению, на складе нет столько выбранного количества товара");
        }
    }


    @Transactional
    public void deleteItemFromUserCart(Long productId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Порльзователь не найден!"));

        CartItem cartItem = user.getCart().getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Не удалось найти товар для удаления"));

        
        
        
        user.getCart().getItems().remove(cartItem);

    }

}
