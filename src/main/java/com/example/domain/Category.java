package com.example.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
//    МОЛОЧНЫЕ_ПРОДУКТЫ, ХЛЕБ, ГОТОВЫЕ_БЛЮДА, НАПИТКИ;


    МОЛОЧКА("Молочные продукты"),
    ХЛЕБ("Хлебобулочные изделия"),
    ГОТОВЫЕ_БЛЮДА("Готовые блюда"),
    НАПИТКИ("Напитки");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue // Благодаря этой аннотации Jackson в JSON отправит красивый текст, а не ГОТОВЫЕ_БЛЮДА
    public String getDisplayName() {
        return displayName;
    }



    // Этот метод переберет все варианты и найдет нужный, игнорируя регистр
    @JsonCreator
    public static Category fromString(String value) {
        if (value == null) {
            return null;
        }
        for (Category category : Category.values()) {
            // Проверяем совпадение с красивым именем ("Напитки") ИЛИ с техническим именем ("НАПИТКИ")
            if (category.displayName.equalsIgnoreCase(value) || category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }
}
