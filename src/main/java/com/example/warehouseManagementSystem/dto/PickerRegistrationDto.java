package com.example.warehouseManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PickerRegistrationDto {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 50, message = "Имя должно быть от 2 до 50 символов") // min = 2 убираем
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    // Отчество не всегда обязательно (например, для иностранных граждан),
    // поэтому @NotBlank не ставим, но ограничим размер, если оно передано
    @Size(max = 50, message = "Отчество не должно превышать 50 символов")
    private String middlename;

    @NotNull(message = "Дата рождения обязательна для заполнения")
    @Past(message = "Дата рождения должна быть в прошлом") // Защита от дат из будущего [health]
    @JsonFormat(pattern = "d-M-yyyy")
    private LocalDate birthDate;

    public PickerRegistrationDto() {
    }

    public PickerRegistrationDto(String firstName, String lastName, String middlename, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middlename = middlename;
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }
}
