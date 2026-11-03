package com.example.repository;

import com.example.domain.Category;
import com.example.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    Product findProductById(Long id);

    List<Product> findByCategory(Category category);
}
