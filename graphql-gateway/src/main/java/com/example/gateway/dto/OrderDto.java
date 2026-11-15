package com.example.gateway.dto;

import java.util.List;

public record OrderDto(
        String id,
        String clientId,
        String status,
        Double totalAmount,
        String createdAt,
        List<OrderItemDto> items
) {}
