package com.example.gateway.dto;

public record AddReviewInput(
        String bookId,
        Integer rating,
        String text
) {}
