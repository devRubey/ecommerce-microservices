package com.devrubey.order_service.client;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductClient(@Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.productServiceUrl = productServiceUrl;
    }

    // Get a product by ID from product-service
    public ProductDto getProductById(Long productId) {
        String url = productServiceUrl + "/api/products/" + productId;
        ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);
        if (response == null || response.getData() == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        return convertToProductDto(response.getData());
    }

    // Helper to convert the response data to ProductDto
    private ProductDto convertToProductDto(Object data) {
        if (data instanceof java.util.LinkedHashMap) {
            java.util.LinkedHashMap<?, ?> map = (java.util.LinkedHashMap<?, ?>) data;
            ProductDto dto = new ProductDto();
            dto.setId(((Number) map.get("id")).longValue());
            dto.setName((String) map.get("name"));
            dto.setPrice(new BigDecimal(map.get("price").toString()));
            dto.setStockQuantity((Integer) map.get("stockQuantity"));
            return dto;
        }
        throw new RuntimeException("Could not parse product response");
    }

    // ── Inner classes to hold response data ──────────────────────────

    @Data
    public static class ApiResponse {
        private String message;
        private Object data;
    }

    @Data
    public static class ProductDto {
        private Long id;
        private String name;
        private BigDecimal price;
        private int stockQuantity;
    }
}