package com.example.gateway.client;

import com.example.gateway.dto.AuthorStatisticsDto;
import com.example.gateway.dto.BookStatisticsDto;
import com.example.gateway.dto.SalesStatisticsDto;
import com.example.gateway.dto.SalesSummaryDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MockAnalyticsClient {

    private final List<SalesStatisticsDto> sales = List.of(
            new SalesStatisticsDto("2026-11-01", 5, 495.00),
            new SalesStatisticsDto("2026-11-02", 3, 260.00)
    );

    private final List<BookStatisticsDto> books = List.of(
            new BookStatisticsDto("b1", 12, 426.00),
            new BookStatisticsDto("b2", 8, 224.00),
            new BookStatisticsDto("b3", 4, 128.00)
    );

    private final List<AuthorStatisticsDto> authors = List.of(
            new AuthorStatisticsDto("a2", 12, 352.00),
            new AuthorStatisticsDto("a1", 12, 426.00)
    );

    public List<SalesStatisticsDto> getSalesStatistics(
            LocalDate from, LocalDate to
    ) {
        return sales.stream()
                .filter(s -> {
                    LocalDate date = LocalDate.parse(s.periodDate());
                    return !date.isBefore(from) && !date.isAfter(to);
                })
                .toList();
    }

    public List<BookStatisticsDto> getPopularBooks(int limit) {
        return books.stream()
                .limit(Math.max(0, limit))
                .toList();
    }

    public List<AuthorStatisticsDto> getPopularAuthors(int limit) {
        return authors.stream()
                .limit(Math.max(0, limit))
                .toList();
    }

    public SalesSummaryDto getSalesSummary(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Некорректный период статистики");
        }

        var filtered = getSalesStatistics(from, to);

        int totalOrders = filtered.stream()
                .mapToInt(SalesStatisticsDto::totalOrders)
                .sum();

        double totalRevenue = filtered.stream()
                .mapToDouble(SalesStatisticsDto::totalRevenue)
                .sum();

        double averageOrderValue = totalOrders == 0
                ? 0.0
                : Math.round(totalRevenue / totalOrders * 100.0) / 100.0;

        return new SalesSummaryDto(
                totalOrders,
                totalRevenue,
                averageOrderValue
        );
    }

}
