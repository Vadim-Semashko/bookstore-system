package com.example.gateway.dto;

public record CreateOrderItemInput(
        String bookId,
        Integer quantity
) {}
