package com.example.warehouseManagementSystem.repository;

import com.example.warehouseManagementSystem.domain.PickingList;
import com.example.warehouseManagementSystem.domain.PickingListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickingListRepository extends JpaRepository<PickingList, Long> {
    Optional<PickingList> findFirstByPickingListStatusOrderByCreatedAtAsc(PickingListStatus status);
}
