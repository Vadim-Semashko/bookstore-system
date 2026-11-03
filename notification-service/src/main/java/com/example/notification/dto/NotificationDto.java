package com.example.notification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        UUID userId,
        String type,
        String message,
        String status,
        LocalDateTime createdAt
) {}
