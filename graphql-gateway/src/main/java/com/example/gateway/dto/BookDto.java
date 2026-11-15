package com.example.gateway.dto;

public record BookDto(
        String id,
        String title,
        String isbn,
        Double price,
        Integer stockQuantity,
        Double rating,
        AuthorDto author,
        CategoryDto category
) {}