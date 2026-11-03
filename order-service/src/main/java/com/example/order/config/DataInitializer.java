package com.example.order.config;

import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeOrders(OrderRepository orderRepository) {
        return args -> {
            if (orderRepository.count() > 0) {
                return;
            }

            Order order = new Order();
            order.setClientId(
                    UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
            );
            order.setStatus("PAID");
            order.setCreatedAt(LocalDateTime.now());

            OrderItem item1 = new OrderItem();
            item1.setOrder(order);
            item1.setBookId(UUID.randomUUID());
            item1.setQuantity(2);
            item1.setPrice(new BigDecimal("35.50"));

            OrderItem item2 = new OrderItem();
            item2.setOrder(order);
            item2.setBookId(UUID.randomUUID());
            item2.setQuantity(1);
            item2.setPrice(new BigDecimal("28.00"));

            order.getItems().add(item1);
            order.getItems().add(item2);

            order.setTotalAmount(new BigDecimal("99.00"));

            orderRepository.save(order);
        };
    }
}
