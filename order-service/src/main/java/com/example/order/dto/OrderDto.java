package com.example.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        UUID clientId,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItemDto> items
) {}
