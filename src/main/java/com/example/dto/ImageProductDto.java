package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ImageProductDto {

    @NotBlank(message = "Ссылка на изображение не должна быть пустой")
    @Size(max = 512, message = "Ссылка слишком длинная")
    private String imageUrl;

    private boolean isMain = false;// главная ли фотография

    // Конструкторы, Геттеры и Сеттеры
    public ImageProductDto() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

}
