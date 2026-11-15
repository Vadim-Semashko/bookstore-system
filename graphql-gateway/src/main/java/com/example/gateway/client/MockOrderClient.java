package com.example.gateway.client;

import com.example.gateway.dto.OrderDto;
import com.example.gateway.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MockOrderClient {

    private static final String TEST_CLIENT_ID = "660e8400-e29b-41d4-a716-446655440000";

    private final List<OrderDto> orders = List.of(
            new OrderDto(
                    "770e8400-e29b-41d4-a716-446655440001",
                    TEST_CLIENT_ID,
                    "PAID",
                    99.00,
                    "2026-11-01T12:30:00",
                    List.of(
                            new OrderItemDto(
                                    "880e8400-e29b-41d4-a716-446655440001",
                                    "b1",
                                    2,
                                    35.50
                            ),
                            new OrderItemDto(
                                    "880e8400-e29b-41d4-a716-446655440002",
                                    "b2",
                                    1,
                                    28.00
                            )
                    )
            ),
            new OrderDto(
                    "770e8400-e29b-41d4-a716-446655440002",
                    TEST_CLIENT_ID,
                    "CREATED",
                    32.00,
                    "2026-11-02T15:10:00",
                    List.of(
                            new OrderItemDto(
                                    "880e8400-e29b-41d4-a716-446655440003",
                                    "b3",
                                    1,
                                    32.00
                            )
                    )
            )
    );

    public List<OrderDto> getOrdersByClientId(String clientId) {
        return orders.stream()
                .filter(order -> order.clientId().equals(clientId))
                .toList();
    }

    public Optional<OrderDto> getOrderById(String id) {
        return orders.stream()
                .filter(order -> order.id().equals(id))
                .findFirst();
    }

    public String getTestClientId() {
        return TEST_CLIENT_ID;
    }
}
