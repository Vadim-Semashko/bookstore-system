package com.example.catalog.dto;

import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String description
) {}