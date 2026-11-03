package com.example.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDto(
        UUID id,
        UUID orderId,
        BigDecimal amount,
        String status,
        LocalDateTime paymentDate
) {}
