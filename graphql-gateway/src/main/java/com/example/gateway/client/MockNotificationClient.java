package com.example.gateway.client;

import com.example.gateway.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockNotificationClient {

    private final List<NotificationDto> notifications = List.of(
            new NotificationDto(
                    "n1",
                    "550e8400-e29b-41d4-a716-446655440000",
                    "REGISTRATION",
                    "Добро пожаловать в книжный магазин!",
                    "SENT",
                    "2026-11-01T10:00:00"
            ),
            new NotificationDto(
                    "n2",
                    "550e8400-e29b-41d4-a716-446655440000",
                    "ORDER_STATUS",
                    "Ваш заказ успешно оплачен.",
                    "SENT",
                    "2026-11-02T12:00:00"
            )
    );

    public List<NotificationDto> getNotificationsByUserId(String userId) {
        return notifications.stream()
                .filter(notification -> notification.userId().equals(userId))
                .toList();
    }
}
