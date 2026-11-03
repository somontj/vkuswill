package com.example.repository;

import com.example.domain.Cart;
import com.example.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Этот метод работает нормально, если он вам нужен:
    List<CartItem> findCartItemByCart(Cart cart);

    // Этот аннотированный метод сработает, потому что указывает точный путь через корзину к пользователю:
    @Query("SELECT ci FROM CartItem ci " +
            "JOIN FETCH ci.product " +
            "WHERE ci.cart.user.id = :userId")
    List<CartItem> findCartItemByUserId(@Param("userId") Long userId);
}
