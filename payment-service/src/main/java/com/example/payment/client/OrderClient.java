package com.example.payment.client;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderClient {

    OrderInfo getOrder(UUID orderId);

    void markOrderPaid(UUID orderId);

    record OrderInfo(
            UUID id,
            String status,
            BigDecimal totalAmount
    ) {}
}
