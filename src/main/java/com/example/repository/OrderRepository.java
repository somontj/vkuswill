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





    @Query("SELECT o FROM Order o") 
    List<Order> getOrder();


    
    List<Order> findByUser_Id(Long userId);

    
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findOrderWithItems(@Param("orderId") Long orderId);
}
