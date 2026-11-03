package com.example.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesStatisticsDto(
        LocalDate periodDate,
        Integer totalOrders,
        BigDecimal totalRevenue
) {}