package com.example.order.controller;

import com.example.order.client.MockCatalogClient;
import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderDto;
import com.example.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MockCatalogClient mockCatalogClient;

    @GetMapping
    public List<OrderDto> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable UUID id
    ) {
        return ResponseEntity.of(
                orderService.getOrderById(id)
        );
    }

    @GetMapping("/client/{clientId}")
    public List<OrderDto> getClientOrders(
            @PathVariable UUID clientId
    ) {
        return orderService.getOrdersByClientId(clientId);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(201)
                .body(orderService.createOrder(request));
    }

    @PostMapping("/{id}/cancel")
    public OrderDto cancelOrder(@PathVariable UUID id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/mock-stock/{bookId}")
    public int getMockStock(@PathVariable UUID bookId) {
        return mockCatalogClient.getAvailableStock(bookId);
    }


}
