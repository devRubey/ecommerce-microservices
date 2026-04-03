package com.devrubey.product_service;

import com.devrubey.product_service.controller.ProductController;
import com.devrubey.product_service.dto.ProductRequest;
import com.devrubey.product_service.dto.ProductResponse;
import com.devrubey.product_service.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponse sampleResponse;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleResponse = ProductResponse.builder()
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

    @Test
    @DisplayName("GET /api/products - should return 200 and products")
    void getAllProducts_success() {
        Page<ProductResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(productService.getAllProducts(0, 10)).thenReturn(page);

        ResponseEntity<?> response = productController.getAllProducts(0, 10);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(productService, times(1)).getAllProducts(0, 10);
    }

    @Test
    @DisplayName("GET /api/products/1 - should return product by ID")
    void getProductById_success() {
        when(productService.getProductById(1L)).thenReturn(sampleResponse);

        ResponseEntity<?> response = productController.getProductById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("POST /api/products - should create product and return 201")
    void createProduct_success() {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(sampleResponse);

        ResponseEntity<?> response = productController.createProduct(sampleRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    @DisplayName("PUT /api/products/1 - should update product and return 200")
    void updateProduct_success() {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(sampleResponse);

        ResponseEntity<?> response = productController.updateProduct(1L, sampleRequest);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(productService, times(1)).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/products/1 - should delete product and return 200")
    void deleteProduct_success() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @DisplayName("GET /api/products/search/category - should return products by category")
    void searchByCategory_success() {
        Page<ProductResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(productService.searchByCategory(eq("Electronics"), eq(0), eq(10))).thenReturn(page);

        ResponseEntity<?> response = productController.searchByCategory("Electronics", 0, 10);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        verify(productService, times(1)).searchByCategory("Electronics", 0, 10);
    }
}