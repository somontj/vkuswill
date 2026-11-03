package com.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Наименование товара не должно быть пустым")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Категория товара должна быть указана")
    private Category category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Цена не должна быть пустой")
    @Positive(message = "Цена должна быть больше нуля")
    private BigDecimal price;

    @Column(name = "stock_quantity") // Хорошая практика: явно указывать snake_case для БД
    @NotNull(message = "Количество на складе не должно быть пустым")
    @Min(value = 0, message = "Количество товара на складе не может быть меньше 0") // Защита от отрицательного склада
    private Integer stockQuantity;

    @Column(name = "created_at", updatable = false)  // Никогда не меняется, дата добавление товара
    private LocalDateTime createdAt;

    @Column(name = "updated_at")          // Обновляется при каждом изменении
    private LocalDateTime updatedAt;     // дата добавление товара

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageProduct> imageProducts = new ArrayList<>();

    @NotNull(message = "Укажите ячейку товара для привязки в ячейку стеллажа")
    private String storageLocation;


    public void addImage(ImageProduct imageProduct) {
        this.imageProducts.add(imageProduct);
        imageProduct.setProduct(this);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<ImageProduct> getImageProducts() {
        return imageProducts;
    }

    public void setImageProducts(List<ImageProduct> imageProducts) {
        this.imageProducts = imageProducts;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
