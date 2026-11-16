package com.example.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RestockBookRequest(
        @NotNull @Min(1) Integer quantity
) {}
