package com.example.gateway.client;

import com.example.gateway.dto.CreateNotificationInput;
import com.example.gateway.dto.NotificationDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MockNotificationClient {

    private final List<NotificationDto> notifications =
            new ArrayList<>(List.of(
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
            ));

    public synchronized List<NotificationDto> getNotificationsByUserId(
            String userId
    ) {
        return notifications.stream()
                .filter(n -> n.userId().equals(userId))
                .toList();
    }

    public synchronized NotificationDto createNotification(
            CreateNotificationInput input
    ) {
        if (input == null ||
                input.userId() == null || input.userId().isBlank() ||
                input.type() == null || input.type().isBlank() ||
                input.message() == null || input.message().isBlank()) {
            throw new IllegalArgumentException(
                    "Некорректные данные уведомления"
            );
        }

        NotificationDto notification = new NotificationDto(
                UUID.randomUUID().toString(),
                input.userId(),
                input.type(),
                input.message(),
                "UNREAD",
                LocalDateTime.now().toString()
        );

        notifications.add(notification);
        return notification;
    }

    public synchronized NotificationDto markNotificationRead(
            String notificationId,
            String userId
    ) {
        NotificationDto oldNotification = notifications.stream()
                .filter(n -> n.id().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Уведомление не найдено"
                ));

        if (!oldNotification.userId().equals(userId)) {
            throw new IllegalStateException(
                    "Уведомление принадлежит другому пользователю"
            );
        }

        if ("READ".equals(oldNotification.status())) {
            throw new IllegalStateException(
                    "Уведомление уже прочитано"
            );
        }

        if (!"UNREAD".equals(oldNotification.status()) &&
                !"SENT".equals(oldNotification.status())) {
            throw new IllegalStateException(
                    "Недопустимый статус уведомления"
            );
        }

        NotificationDto updated = new NotificationDto(
                oldNotification.id(),
                oldNotification.userId(),
                oldNotification.type(),
                oldNotification.message(),
                "READ",
                oldNotification.createdAt()
        );

        notifications.replaceAll(n ->
                n.id().equals(notificationId) ? updated : n
        );

        return updated;
    }
}
