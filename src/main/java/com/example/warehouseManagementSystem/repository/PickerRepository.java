package com.example.warehouseManagementSystem.repository;

import com.example.warehouseManagementSystem.domain.Picker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickerRepository extends JpaRepository<Picker, Long> {
    Optional<Picker> findByBadgeBarcode(String badgeBarcode);
}
