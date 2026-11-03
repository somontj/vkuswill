package com.example.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_order", nullable = false, unique = true)
    private String numberOrder = UUID.randomUUID().toString();


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "address_delivery")
    private String address;

    @Column(name = "comment")
    private String comment;

    @Column(name = "total_price")
    private BigDecimal totalPrice;


    public Order() {
    }

    public Order(Long id, String numberOrder, LocalDateTime createdAt, LocalDateTime updatedAt, User user, List<OrderItem> orderItems, OrderStatus orderStatus, String address, String comment, BigDecimal totalPrice) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.address = address;
        this.comment = comment;
        this.totalPrice = totalPrice;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.numberOrder == null) {
            
            long randomNumber = java.util.concurrent.ThreadLocalRandom.current()
                    .nextLong(10_000_000L, 100_000_000L);
            this.numberOrder = String.valueOf(randomNumber);
        }
    }

    @PreUpdate
    protected void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(String numberOrder) {
        this.numberOrder = numberOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
