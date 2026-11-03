package com.example.repository;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    // Явно указываем выборку элементов заказа (i) через JOIN
//    @Query("SELECT i FROM Order o JOIN o.orderItems i WHERE o.userId = :id")
//    List<OrderItem> getOrderItemsByUserId(@Param("id") Long id);

    @Query("SELECT o FROM Order o") // Напишите здесь ваш JPQL или SQL запрос
    List<Order> getOrder();


    // Получить все заказы конкретного пользователя
    List<Order> findByUser_Id(Long userId);

    // Пример FETCH-запроса, если захотите вытащить заказ сразу со всеми его элементами
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findOrderWithItems(@Param("orderId") Long orderId);
}
