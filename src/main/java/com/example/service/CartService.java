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


        // 2. НАДЕЖНЫЙ ШАГ: Находим этого пользователя в БД по ID, чтобы Hibernate "увидел" его
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден в БД!"));

        // 3. Теперь логика создания корзины отработает идеально
        if (user.getCart() == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            user.setCart(newCart);
        }

        Cart cart = user.getCart();

        // ... дальше твой остальной код поиска продукта, цикла и сохранения ...
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

//        cartRepository.save(cart);    Благодаря  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//       и также там стои @Transactional           private List<CartItem> items = new ArrayList<>();
    }


//    @Transactional
//    public void linkSessionCardAndMultipleItems(Long id, List<CartItemDto> cartItemDto) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден в БД!"));
//
//        // 3. Теперь логика создания корзины отработает идеально
//        if (user.getCart() == null) {
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            user.setCart(newCart);
//        }
//
//        Cart cart = user.getCart();
//
//        for (CartItemDto itemDtos : cartItemDto) {
//
//            // ... дальше твой остальной код поиска продукта, цикла и сохранения ...
//            Product product = productRepository.findById(itemDtos.getProductId())
//                    .orElseThrow(() -> new IllegalArgumentException("Добавляемый товар не найден!"));
//
//            boolean itemExists = false;
//            for (CartItem item : cart.getItems()) {
//                if (item.getProduct().getId().equals(product.getId())) {
//                    item.setQuantity(item.getQuantity() + itemDtos.getQuantity());
//                    itemExists = true;
//                    break;
//                }
//            }
//
//            if (!itemExists) {
//                CartItem newItem = new CartItem();
//                newItem.setProduct(product);
//                newItem.setQuantity(itemDtos.getQuantity());
//                cart.addItem(newItem);
//            }
//        }
//
//        cartRepository.save(cart);
//    }

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

        // 1. Собираем все ID продуктов из пришедшего списка DTO
        List<Long> productIds = cartItemDtos.stream()
                .map(CartItemDto::getProductId)
                .toList();

        // 2. Достаем ВСЕ нужные продукты ОДНИМ запросом вместо пачки запросов в цикле
        List<Product> products = productRepository.findAllById(productIds);

        // Превращаем в Map для быстрого поиска по ID: Map<Long, Product>
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // 3. Теперь бежим по нашему циклу без обращений к БД
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

//        if (user.getCart() == null) {
//            throw new IllegalArgumentException("У пользователя нет активной корзины");
//        }

        // Ищем нужный товар и обновляем количество
        CartItem cartItem = user.getCart().getItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartItemDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Товар в корзине не найден"));

        Product product = cartItem.getProduct();

        if (cartItemDto.getQuantity() == 0) {
            // Благодаря orphanRemoval = true, строка сама удалится из БД
            user.getCart().getItems().remove(cartItem);
//            cartItemRepository.delete(cartItem);
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

        // 3. Удаляем из коллекции.
        // Внимание: чтобы товар удалился из БД, у вас в сущности Cart над полем items
        // обязательно должно стоять @OneToMany(..., orphanRemoval = true)
        user.getCart().getItems().remove(cartItem);
//        cartItemRepository.delete(cartItem);
    }

}
