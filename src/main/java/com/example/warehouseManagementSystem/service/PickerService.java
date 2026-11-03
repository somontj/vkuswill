package com.example.warehouseManagementSystem.service;

import com.example.warehouseManagementSystem.domain.Picker;
import com.example.warehouseManagementSystem.domain.PickerStatus;
import com.example.warehouseManagementSystem.dto.PickerRegistrationDto;
import com.example.warehouseManagementSystem.repository.PickerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PickerService {
    private final PickerRepository pickerRepository;

    public PickerService(PickerRepository pickerRepository) {
        this.pickerRepository = pickerRepository;
    }

    @Transactional
    public void registerNewPicker(PickerRegistrationDto dto) {

        Picker picker = new Picker();

        picker.setFirstName(dto.getFirstName());
        picker.setLastName(dto.getLastName());
        picker.setMiddlename(dto.getMiddlename());
        picker.setBirthDate(dto.getBirthDate());
        picker.setStatus(PickerStatus.OFF);

        picker = pickerRepository.save(picker);


       picker.setBadgeBarcode("PICKER-" + (100000 + picker.getId()));

       pickerRepository.save(picker);

    }
}
