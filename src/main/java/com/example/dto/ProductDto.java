package com.example.dto;

import com.example.domain.Category;
import com.example.domain.Product;

import java.math.BigDecimal;

public class ProductDto {


    private String name;

    private String description;

    private Category category;

    private BigDecimal price;

    private Integer stockQuantity;

    private String storageLocation;

    public ProductDto() {

    }

    public ProductDto(Product product) {

        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.storageLocation = product.getStorageLocation();
    }










    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
