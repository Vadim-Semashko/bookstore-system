package com.example.order.service;

import com.example.order.client.CatalogClient;
import com.example.order.dto.CreateOrderItemRequest;
import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderDto;
import com.example.order.dto.OrderItemDto;
import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<OrderDto> getOrderById(UUID id) {
        return orderRepository.findById(id)
                .map(this::toDto);
    }

    public List<OrderDto> getOrdersByClientId(UUID clientId) {
        return orderRepository.findByClientId(clientId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {

        if (request == null || request.clientId() == null ||
                request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Заказ не может быть пустым");
        }

        Set<UUID> bookIds = new HashSet<>();

        for (CreateOrderItemRequest item : request.items()) {
            if (item == null || item.bookId() == null ||
                    item.quantity() == null || item.quantity() <= 0) {
                throw new IllegalArgumentException("Некорректная позиция заказа");
            }

            if (!bookIds.add(item.bookId())) {
                throw new IllegalArgumentException("Повторяющаяся книга в заказе");
            }
        }

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setClientId(request.clientId());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        try {
            for (CreateOrderItemRequest requestedItem : request.items()) {

                BigDecimal price = catalogClient.reserveBook(
                        orderId,
                        requestedItem.bookId(),
                        requestedItem.quantity()
                );

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setBookId(requestedItem.bookId());
                item.setQuantity(requestedItem.quantity());
                item.setPrice(price);

                order.getItems().add(item);

                total = total.add(price.multiply(BigDecimal.valueOf(requestedItem.quantity())));
            }

            order.setTotalAmount(total);

            order.setId(orderId);

            return toDto(orderRepository.save(order));

        } catch (RuntimeException ex) {
            catalogClient.releaseReservation(orderId);
            throw ex;
        }
    }

    @Transactional
    public OrderDto cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if ("CANCELLED".equals(order.getStatus())) {
            throw new IllegalStateException("Заказ уже отменён");
        }

        if (!"CREATED".equals(order.getStatus())) {
            throw new IllegalStateException("Можно отменить только неоплаченный заказ");
        }

        catalogClient.releaseReservation(orderId);

        order.setStatus("CANCELLED");

        return toDto(orderRepository.save(order));
    }


    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getClientId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(this::toItemDto)
                        .toList()
        );
    }

    private OrderItemDto toItemDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getBookId(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
