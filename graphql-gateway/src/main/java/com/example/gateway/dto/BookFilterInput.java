package com.example.gateway.dto;

public record BookFilterInput(
        String title,
        String authorId,
        String categoryId,
        Double minPrice,
        Double maxPrice
) {}
