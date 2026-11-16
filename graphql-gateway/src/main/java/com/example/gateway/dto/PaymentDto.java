package com.example.gateway.dto;

public record PaymentDto(
        String id,
        String orderId,
        Double amount,
        String status,
        String paymentDate
) {}
