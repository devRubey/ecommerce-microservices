package com.devrubey.order_service.service;

import com.devrubey.order_service.client.ProductClient;
import com.devrubey.order_service.dto.*;
import com.devrubey.order_service.exception.*;
import com.devrubey.order_service.model.Order;
import com.devrubey.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    // ── Helper ────────────────────────────────────────────────────────

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    // ── Place Order ───────────────────────────────────────────────────

    /** Placing a new order */
    public OrderResponse placeOrder(OrderRequest request) {

        // Step 1: Call product-service to get product details
        ProductClient.ProductDto product = productClient.getProductById(request.getProductId());

        // Step 2: Check if enough stock is available
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(product.getName(), product.getStockQuantity());
        }

        // Step 3: Calculate total price
        BigDecimal totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // Step 4: Build and save the order
        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .productName(product.getName())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .status(Order.OrderStatus.PENDING)
                .build();

        return toResponse(orderRepository.save(order));
    }

    // ── Get Orders ────────────────────────────────────────────────────

    /** Getting all orders (paginated) */
    public Page<OrderResponse> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findAll(pageable).map(this::toResponse);
    }

    /** Getting a single order by ID */
    public OrderResponse getOrderById(Long id) {
        return toResponse(findOrderOrThrow(id));
    }

    /** Getting all orders for a specific user */
    public Page<OrderResponse> getOrdersByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    /** Get all orders by status */
    public Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findByStatus(status, pageable).map(this::toResponse);
    }

    // ── Update Order ──────────────────────────────────────────────────

    /** Confirming an order */
    public OrderResponse confirmOrder(Long id) {
        Order order = findOrderOrThrow(id);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        return toResponse(orderRepository.save(order));
    }

    /** Canceling an order */
    public OrderResponse cancelOrder(Long id) {
        Order order = findOrderOrThrow(id);
        if (order.getStatus() == Order.OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel a confirmed order.");
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }
}