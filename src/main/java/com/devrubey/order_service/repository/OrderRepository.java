package com.devrubey.order_service.repository;

import com.devrubey.order_service.model.Order;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Getting all orders for a specific user (paginated)
    Page<Order> findByUserId(Long userId, Pageable pageable);

    // Getting all orders for a specific product (paginated)
    Page<Order> findByProductId(Long productId, Pageable pageable);

    // Getting all orders by status (paginated)
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);
}