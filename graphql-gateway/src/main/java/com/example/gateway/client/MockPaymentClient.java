package com.example.gateway.client;

import com.example.gateway.dto.OrderDto;
import com.example.gateway.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MockPaymentClient {

    private final MockOrderClient orderClient;

    private final Map<String, PaymentDto> payments = new HashMap<>();

    public synchronized PaymentDto payOrder(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Идентификатор заказа обязателен");
        }

        if (payments.containsKey(orderId)) {
            throw new IllegalStateException("Заказ уже оплачен");
        }

        OrderDto order = orderClient.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (!"CREATED".equals(order.status())) {
            throw new IllegalStateException("Оплатить можно только заказ со статусом CREATED");
        }

        if (order.totalAmount() == null ||
                !Double.isFinite(order.totalAmount()) ||
                order.totalAmount() <= 0) {
            throw new IllegalStateException("Некорректная сумма заказа");
        }

        orderClient.markOrderPaid(orderId);

        PaymentDto payment = new PaymentDto(
                UUID.randomUUID().toString(),
                orderId,
                order.totalAmount(),
                "COMPLETED",
                LocalDateTime.now().toString()
        );

        payments.put(orderId, payment);

        return payment;
    }
}
