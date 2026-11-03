package com.example.repository;

import com.example.domain.Cart;
import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
//    List<Cart> findCartByUser(User user);
}
