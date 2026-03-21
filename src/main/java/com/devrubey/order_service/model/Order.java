package com.devrubey.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;        // who placed the order

    @Column(nullable = false)
    private Long productId;     // what product was ordered

    @Column(nullable = false)
    private String productName; // name of the product at time of order

    @Column(nullable = false)
    private int quantity;       // how many units ordered

    @Column(nullable = false)
    private BigDecimal totalPrice; // quantity × product price

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        PENDING,    // just placed
        CONFIRMED,  // payment confirmed
        CANCELLED   // cancelled
    }
}