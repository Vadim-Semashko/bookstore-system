
package com.example.payment.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MockOrderClient implements OrderClient {

    private final Map<UUID, OrderInfo> orders = new HashMap<>();

    public MockOrderClient(@Value("${payment.mock-order-id}") UUID testOrderId) {
        orders.put(testOrderId, new OrderInfo(
                testOrderId,
                "CREATED",
                new BigDecimal("99.00")
        ));
    }

    @Override
    public synchronized OrderInfo getOrder(UUID orderId) {
        OrderInfo order = orders.get(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден");
        }

        return order;
    }

    @Override
    public synchronized void markOrderPaid(UUID orderId) {
        OrderInfo order = getOrder(orderId);

        if (!"CREATED".equals(order.status())) {
            throw new IllegalStateException("Заказ нельзя перевести в статус PAID");
        }

        orders.put(orderId, new OrderInfo(
                order.id(),
                "PAID",
                order.totalAmount()
        ));
    }
}
