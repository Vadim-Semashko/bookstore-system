package com.example.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID id,
        UUID bookId,
        Integer quantity,
        BigDecimal price
) {}
