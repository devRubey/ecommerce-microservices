package com.devrubey.product_service.repository;

import com.devrubey.product_service.model.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by category (paginated)
    Page<Product> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    // Search by name (paginated)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}