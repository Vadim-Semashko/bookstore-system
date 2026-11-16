package com.example.analytics.dto;

import java.math.BigDecimal;

public record SalesSummaryDto(
        Long totalOrders,
        BigDecimal totalRevenue,
        BigDecimal averageOrderValue
) {}
