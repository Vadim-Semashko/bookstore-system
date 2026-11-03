package com.example.analytics.controller;

import com.example.analytics.dto.AuthorStatisticsDto;
import com.example.analytics.dto.BookStatisticsDto;
import com.example.analytics.dto.SalesStatisticsDto;
import com.example.analytics.service.AnalyticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/internal/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales")
    public List<SalesStatisticsDto> getSales(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        return analyticsService.getSalesStatistics(from, to);
    }

    @GetMapping("/books")
    public List<BookStatisticsDto> getPopularBooks(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return analyticsService.getPopularBooks(limit);
    }

    @GetMapping("/authors")
    public List<AuthorStatisticsDto> getPopularAuthors(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return analyticsService.getPopularAuthors(limit);
    }
}
