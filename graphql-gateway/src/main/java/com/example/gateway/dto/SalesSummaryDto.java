package com.example.gateway.dto;

public record SalesSummaryDto(
        Integer totalOrders,
        Double totalRevenue,
        Double averageOrderValue
) {}
