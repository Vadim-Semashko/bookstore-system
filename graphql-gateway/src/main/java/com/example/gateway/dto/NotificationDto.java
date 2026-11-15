package com.example.gateway.dto;

public record NotificationDto(
        String id,
        String userId,
        String type,
        String message,
        String status,
        String createdAt
) {}
