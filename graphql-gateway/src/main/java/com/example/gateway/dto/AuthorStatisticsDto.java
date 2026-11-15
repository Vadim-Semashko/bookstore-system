package com.example.gateway.dto;

public record AuthorStatisticsDto(
        String authorId,
        Integer totalSold,
        Double totalRevenue
) {}
