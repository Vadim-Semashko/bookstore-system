package com.example.analytics.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AuthorStatisticsDto(
        UUID authorId,
        Integer totalSold,
        BigDecimal totalRevenue
) {}