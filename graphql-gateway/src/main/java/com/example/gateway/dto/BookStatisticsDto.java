package com.example.gateway.dto;

public record BookStatisticsDto(
        String bookId,
        Integer totalSold,
        Double totalRevenue
) {}
