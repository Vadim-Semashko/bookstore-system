package com.example.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBookPriceRequest(
        @NotNull @DecimalMin("0.01") BigDecimal price
) {}
