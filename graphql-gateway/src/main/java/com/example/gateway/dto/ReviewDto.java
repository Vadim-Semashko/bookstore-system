package com.example.gateway.dto;

public record ReviewDto(
        String id,
        String clientId,
        String bookId,
        Integer rating,
        String text,
        String status,
        String createdAt
) {}
