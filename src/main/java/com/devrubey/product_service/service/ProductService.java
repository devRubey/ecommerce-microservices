package com.devrubey.product_service.service;

import com.devrubey.product_service.dto.*;
import com.devrubey.product_service.exception.ProductNotFoundException;
import com.devrubey.product_service.model.Product;
import com.devrubey.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ── Helper methods ────────────────────────────────────────────────

    // Finds a product or throws a 404 error
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // Converts a Product entity to a ProductResponse DTO
    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }

    // ── CRUD ──────────────────────────────────────────────────────────

    /** Get all products with pagination */
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    /** Get a single product by ID */
    public ProductResponse getProductById(Long id) {
        return toResponse(findProductOrThrow(id));
    }

    /** Create a new product */
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .build();

        return toResponse(productRepository.save(product));
    }

    /** Update an existing product */
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());

        return toResponse(productRepository.save(product));
    }

    /** Delete a product */
    public void deleteProduct(Long id) {
        findProductOrThrow(id);
        productRepository.deleteById(id);
    }

    // ── Search ────────────────────────────────────────────────────────

    /** Search products by category */
    public Page<ProductResponse> searchByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryContainingIgnoreCase(category, pageable)
                .map(this::toResponse);
    }

    /** Search products by name */
    public Page<ProductResponse> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::toResponse);
    }
}