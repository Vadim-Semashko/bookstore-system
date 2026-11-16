package com.example.gateway.dto;

public record AddBookInput(
        String title,
        String isbn,
        String authorId,
        String categoryId,
        Double price,
        Integer stockQuantity
) {}
