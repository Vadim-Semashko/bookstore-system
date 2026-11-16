package com.example.gateway.client;

import com.example.gateway.dto.CreateOrderInput;
import com.example.gateway.dto.CreateOrderItemInput;
import com.example.gateway.dto.OrderDto;
import com.example.gateway.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MockOrderClient {

    private final MockCatalogClient catalogClient;

    private static final String TEST_CLIENT_ID = "660e8400-e29b-41d4-a716-446655440000";

    private final List<OrderDto> orders = new ArrayList<>(List.of(
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
    ));

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

    public synchronized OrderDto createOrder(CreateOrderInput input) {

        if (input == null || input.items() == null ||
                input.items().isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы одну позицию");
        }

        Set<String> bookIds = new HashSet<>();

        for (CreateOrderItemInput item : input.items()) {
            if (item == null || item.bookId() == null ||
                    item.bookId().isBlank() ||
                    item.quantity() == null ||
                    item.quantity() <= 0) {
                throw new IllegalArgumentException("Некорректная позиция заказа");
            }

            if (!bookIds.add(item.bookId())) {
                throw new IllegalArgumentException("Книга повторяется в заказе");
            }
        }

        String orderId = UUID.randomUUID().toString();
        List<OrderItemDto> orderItems = new ArrayList<>();
        double total = 0;

        try {
            for (CreateOrderItemInput requested : input.items()) {

                Double price = catalogClient.reserveBook(
                        orderId,
                        requested.bookId(),
                        requested.quantity()
                );

                orderItems.add(new OrderItemDto(
                        UUID.randomUUID().toString(),
                        requested.bookId(),
                        requested.quantity(),
                        price
                ));

                total += price * requested.quantity();
            }

            OrderDto order = new OrderDto(
                    orderId,
                    TEST_CLIENT_ID,
                    "CREATED",
                    total,
                    LocalDateTime.now().toString(),
                    List.copyOf(orderItems)
            );

            orders.add(order);
            return order;

        } catch (RuntimeException ex) {
            catalogClient.releaseReservation(orderId);
            throw ex;
        }
    }

    public synchronized OrderDto cancelOrder(String orderId) {

        OrderDto order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if ("CANCELLED".equals(order.status())) {
            throw new IllegalStateException("Заказ уже отменён");
        }

        if (!"CREATED".equals(order.status())) {
            throw new IllegalStateException("Можно отменить только неоплаченный заказ");
        }

        catalogClient.releaseReservation(orderId);

        OrderDto cancelled = new OrderDto(
                order.id(),
                order.clientId(),
                "CANCELLED",
                order.totalAmount(),
                order.createdAt(),
                order.items()
        );

        orders.replaceAll(o ->
                o.id().equals(orderId) ? cancelled : o
        );

        return cancelled;
    }

    public synchronized OrderDto markOrderPaid(String orderId) {

        OrderDto order = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (!"CREATED".equals(order.status())) {
            throw new IllegalStateException("Оплатить можно только заказ со статусом CREATED");
        }

        catalogClient.confirmReservation(orderId);

        OrderDto paidOrder = new OrderDto(
                order.id(),
                order.clientId(),
                "PAID",
                order.totalAmount(),
                order.createdAt(),
                order.items()
        );

        orders.replaceAll(o -> o.id().equals(orderId) ? paidOrder : o);

        return paidOrder;
    }


}
