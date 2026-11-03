package com.example.warehouseManagementSystem.controller;

import com.example.warehouseManagementSystem.dto.PickerRegistrationDto;
import com.example.warehouseManagementSystem.service.PickerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/picker")
public class PickerController {
    private final PickerService pickerService;

    public PickerController(PickerService pickerService) {
        this.pickerService = pickerService;
    }


    @PostMapping("/pickerRegister")
    public ResponseEntity<String> pickerRegister(@Valid @RequestBody PickerRegistrationDto dto) {
        try {
            if (dto.getBirthDate() != null) {
                int age = Period.between(dto.getBirthDate(), LocalDate.now()).getYears();



                if (age < 18) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Регистрация отклонена: сотруднику должно быть не менее 18 лет!");
                }
            }
            pickerService.registerNewPicker(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Сборщик успешно зарегистрирован!");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера: " + exception.getMessage());
        }
    }
}
