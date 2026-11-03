package com.example.warehouseManagementSystem.service;

import com.example.domain.Order;
import com.example.domain.OrderStatus;
import com.example.dto.OrderItemDto;
import com.example.repository.OrderRepository;
import com.example.warehouseManagementSystem.domain.Picker;
import com.example.warehouseManagementSystem.domain.PickerStatus;
import com.example.warehouseManagementSystem.domain.PickingList;
import com.example.warehouseManagementSystem.domain.PickingListStatus;
import com.example.warehouseManagementSystem.dto.PickerListDto;
import com.example.warehouseManagementSystem.repository.PickerRepository;
import com.example.warehouseManagementSystem.repository.PickingListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WareHouseService {
    private final PickerRepository pickerRepository;
    private final PickingListRepository pickingListRepository;
    private final OrderRepository orderRepository;

    public WareHouseService(PickerRepository pickerRepository, PickingListRepository pickingListRepository, OrderRepository orderRepository) {
        this.pickerRepository = pickerRepository;
        this.pickingListRepository = pickingListRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PickerListDto assignNextOrderToPicker(String badgeNumber) {


        Picker picker = pickerRepository.findByBadgeBarcode(badgeNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сборщик таким бейджом " + badgeNumber + " не найден"));

        if (picker.getStatus() == PickerStatus.OFF || picker.getStatus() == PickerStatus.PICKING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сборшик собирает заказ или он не на смене");
        }

        if (picker.getStatus() != PickerStatus.FREE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сборщик сейчас не может взять заказ (статус: " + picker.getStatus() + ")");
        }

        PickingList pickingList = pickingListRepository.findFirstByPickingListStatusOrderByCreatedAtAsc(PickingListStatus.CREATED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Нет доступных заказов для сборки"));

        Order order = pickingList.getOrder();

        order.setOrderStatus(OrderStatus.PICKING);
        pickingList.setPicker(picker);
        pickingList.setPickingListStatus(PickingListStatus.IN_PROGRESS);
        picker.setStatus(PickerStatus.PICKING);

        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(item -> new OrderItemDto(
                        item.getProduct().getName(),
                        item.getPriceProduct(),
                        item.getQuantity()
                ))
                .toList();


        return new PickerListDto(
                order.getUser().getName(),
                order.getUser().getPhone(),
                order.getComment(),
                itemDtos
        );
    }
}
