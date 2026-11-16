package com.example.analytics.service;

import com.example.analytics.dto.AuthorStatisticsDto;
import com.example.analytics.dto.BookStatisticsDto;
import com.example.analytics.dto.SalesStatisticsDto;

import com.example.analytics.dto.SalesSummaryDto;
import com.example.analytics.entity.SalesStatistics;
import com.example.analytics.repository.AuthorStatisticsRepository;
import com.example.analytics.repository.BookStatisticsRepository;
import com.example.analytics.repository.SalesStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final SalesStatisticsRepository salesRepository;
    private final BookStatisticsRepository bookRepository;
    private final AuthorStatisticsRepository authorRepository;

    public List<SalesStatisticsDto> getSalesStatistics(
            LocalDate from, LocalDate to
    ) {
        return salesRepository
                .findByPeriodDateBetweenOrderByPeriodDateAsc(from, to)
                .stream()
                .map(s -> new SalesStatisticsDto(
                        s.getPeriodDate(),
                        s.getTotalOrders(),
                        s.getTotalRevenue()
                ))
                .toList();
    }

    public List<BookStatisticsDto> getPopularBooks(int limit) {
        return bookRepository.findAllByOrderByTotalSoldDesc()
                .stream()
                .limit(limit)
                .map(b -> new BookStatisticsDto(
                        b.getBookId(),
                        b.getTotalSold(),
                        b.getTotalRevenue()
                ))
                .toList();
    }

    public List<AuthorStatisticsDto> getPopularAuthors(int limit) {
        return authorRepository.findAllByOrderByTotalSoldDesc()
                .stream()
                .limit(limit)
                .map(a -> new AuthorStatisticsDto(
                        a.getAuthorId(),
                        a.getTotalSold(),
                        a.getTotalRevenue()
                ))
                .toList();
    }

    public SalesSummaryDto getSalesSummary(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Некорректный период статистики");
        }

        var statistics = salesRepository.findByPeriodDateBetweenOrderByPeriodDateAsc(from, to);

        long totalOrders = statistics.stream()
                .mapToLong(SalesStatistics::getTotalOrders)
                .sum();

        BigDecimal totalRevenue = statistics.stream()
                .map(SalesStatistics::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = totalOrders == 0
                ? BigDecimal.ZERO.setScale(2)
                : totalRevenue.divide(
                BigDecimal.valueOf(totalOrders),
                2,
                RoundingMode.HALF_UP
        );

        return new SalesSummaryDto(
                totalOrders,
                totalRevenue,
                averageOrderValue
        );
    }

}
