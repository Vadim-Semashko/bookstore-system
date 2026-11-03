package com.example.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDto(
        UUID id,
        UUID clientId,
        UUID bookId,
        Integer rating,
        String text,
        String status,
        LocalDateTime createdAt
) {}
