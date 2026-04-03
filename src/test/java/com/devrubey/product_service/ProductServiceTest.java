package com.devrubey.product_service;

import com.devrubey.product_service.dto.ProductRequest;
import com.devrubey.product_service.dto.ProductResponse;
import com.devrubey.product_service.exception.ProductNotFoundException;
import com.devrubey.product_service.model.Product;
import com.devrubey.product_service.repository.ProductRepository;
import com.devrubey.product_service.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("A powerful laptop")
                .price(new BigDecimal("999.99"))
                .category("Electronics")
                .stockQuantity(10)
                .build();

        sampleRequest = ProductRequest.builder()
                .name("Laptop")
                .description("A powerful laptop")
                .price(new BigDecimal("999.99"))
                .category("Electronics")
                .stockQuantity(10)
                .build();
    }

    // ── CREATE ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should create a product successfully")
    void createProduct_success() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(sampleRequest);

        assertThat(response.getName()).isEqualTo("Laptop");
        assertThat(response.getPrice()).isEqualByComparingTo("999.99");
        assertThat(response.getCategory()).isEqualTo("Electronics");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ── READ ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return product when found by ID")
    void getProductById_found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when ID does not exist")
    void getProductById_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── UPDATE ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should update a product successfully")
    void updateProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Gaming Laptop")
                .description("Updated description")
                .price(new BigDecimal("1299.99"))
                .category("Electronics")
                .stockQuantity(5)
                .build();

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when updating non-existent product")
    void updateProduct_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99L, sampleRequest))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ── DELETE ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should delete a product successfully")
    void deleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting non-existent product")
    void deleteProduct_notFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ── PAGINATION ────────────────────────────────────────────────────

    @Test
    @DisplayName("Should return paginated list of products")
    void getAllProducts_paginated() {
        Page<Product> mockPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<ProductResponse> result = productService.getAllProducts(0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop");
    }

    // ── SEARCH ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should search products by category")
    void searchByCategory_success() {
        Page<Product> mockPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findByCategoryContainingIgnoreCase(eq("Electronics"), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<ProductResponse> result = productService.searchByCategory("Electronics", 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("Should search products by name")
    void searchByName_success() {
        Page<Product> mockPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findByNameContainingIgnoreCase(eq("Laptop"), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<ProductResponse> result = productService.searchByName("Laptop", 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop");
    }
}