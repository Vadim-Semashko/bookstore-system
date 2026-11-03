package com.example.analytics.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookStatisticsDto(
        UUID bookId,
        Integer totalSold,
        BigDecimal totalRevenue
) {}