package com.devrubey.order_service.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int available) {
        super("Insufficient stock for '" + productName + "'. Only " + available + " units available.");
    }
}