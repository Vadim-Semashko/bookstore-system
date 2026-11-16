package com.example.gateway.dto;

public record CreateNotificationInput(
        String userId,
        String type,
        String message
) {}
