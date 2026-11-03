package com.example.service;

import com.example.domain.*;
import com.example.repository.CartItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import com.example.warehouseManagementSystem.domain.PickingList;
import com.example.warehouseManagementSystem.domain.PickingListStatus;
import com.example.warehouseManagementSystem.repository.PickingListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final PickingListRepository pickingListRepository;


    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartItemRepository cartItemRepository, PickingListRepository pickingListRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.pickingListRepository = pickingListRepository;
    }


    public List<Order> getOrder() {
        return orderRepository.getOrder();
    }


    @Transactional
    public void createOrder(Long id, String addresss, String comment) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Cart cart = user.getCart();
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice()))
                .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(orderItem -> orderItem.getPriceProduct().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setAddress(addresss);
        order.setComment(comment);
        order.setUser(user);

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

        PickingList pickingList = new PickingList();
        pickingList.setOrder(order);
        pickingList.setPickingListStatus(PickingListStatus.CREATED);

        pickingListRepository.save(pickingList);


    }
}
