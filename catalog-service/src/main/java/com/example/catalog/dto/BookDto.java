package com.example.catalog.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookDto(
        UUID id,
        String title,
        String isbn,
        BigDecimal price,
        Integer stockQuantity,
        BigDecimal rating,
        AuthorDto author,
        CategoryDto category
) {}