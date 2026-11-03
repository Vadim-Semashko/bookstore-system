package com.example.catalog.dto;

import java.util.UUID;

public record AuthorDto(
        UUID id,
        String firstName,
        String lastName,
        String description
) {}