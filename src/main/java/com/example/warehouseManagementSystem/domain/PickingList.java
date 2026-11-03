package com.example.warehouseManagementSystem.domain;

import com.example.domain.Order;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "picking_list")
public class PickingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picker_id")
    private Picker picker;

    @Enumerated(EnumType.STRING)
    private PickingListStatus pickingListStatus;

    private LocalDateTime createdAt;

    @PrePersist
    protected void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    public PickingList() {
    }

    public PickingList(Long id, Order order, Picker picker, PickingListStatus pickingListStatus) {
        this.id = id;
        this.order = order;
        this.picker = picker;
        this.pickingListStatus = pickingListStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Picker getPicker() {
        return picker;
    }

    public void setPicker(Picker picker) {
        this.picker = picker;
    }

    public PickingListStatus getPickingListStatus() {
        return pickingListStatus;
    }

    public void setPickingListStatus(PickingListStatus pickingListStatus) {
        this.pickingListStatus = pickingListStatus;
    }
}
