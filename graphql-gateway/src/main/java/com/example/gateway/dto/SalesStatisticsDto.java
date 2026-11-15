package com.example.gateway.dto;

public record SalesStatisticsDto(
        String periodDate,
        Integer totalOrders,
        Double totalRevenue
) {}
