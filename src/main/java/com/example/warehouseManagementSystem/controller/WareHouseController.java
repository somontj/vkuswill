package com.example.warehouseManagementSystem.controller;

import com.example.warehouseManagementSystem.domain.PickingList;
import com.example.warehouseManagementSystem.dto.PickerListDto;
import com.example.warehouseManagementSystem.repository.PickerRepository;
import com.example.warehouseManagementSystem.service.WareHouseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
public class WareHouseController {
    private final WareHouseService wareHouseService;
    private final PickerRepository pickerRepository;

    public WareHouseController(WareHouseService wareHouseService, PickerRepository pickerRepository) {
        this.wareHouseService = wareHouseService;
        this.pickerRepository = pickerRepository;
    }

    @PostMapping("/picklist")
    public PickerListDto getPickList(@RequestParam String badgeNumber) {
        return wareHouseService.assignNextOrderToPicker(badgeNumber);
    }
}
