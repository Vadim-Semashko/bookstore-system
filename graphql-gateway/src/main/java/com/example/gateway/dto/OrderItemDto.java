package com.example.gateway.dto;

public record OrderItemDto(
        String id,
        String bookId,
        Integer quantity,
        Double price
) {}