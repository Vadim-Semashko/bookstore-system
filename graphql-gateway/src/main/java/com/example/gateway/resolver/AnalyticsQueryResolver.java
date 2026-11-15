package com.example.gateway.resolver;

import com.example.gateway.client.MockAnalyticsClient;
import com.example.gateway.dto.AuthorStatisticsDto;
import com.example.gateway.dto.BookStatisticsDto;
import com.example.gateway.dto.SalesStatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnalyticsQueryResolver {

    private final MockAnalyticsClient analyticsClient;

    @QueryMapping
    public List<SalesStatisticsDto> salesStatistics(@Argument String from, @Argument String to) {
        LocalDate dateFrom = LocalDate.parse(from);
        LocalDate dateTo = LocalDate.parse(to);

        if (dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException(
                    "Start date must not be after end date"
            );
        }

        return analyticsClient.getSalesStatistics(dateFrom, dateTo);
    }

    @QueryMapping
    public List<BookStatisticsDto> popularBooks(@Argument Integer limit) {
        int actualLimit = limit == null ? 10 : limit;

        if (actualLimit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than zero");
        }

        return analyticsClient.getPopularBooks(actualLimit);
    }

    @QueryMapping
    public List<AuthorStatisticsDto> popularAuthors(@Argument Integer limit) {
        int actualLimit = limit == null ? 10 : limit;

        if (actualLimit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than zero");
        }

        return analyticsClient.getPopularAuthors(actualLimit);
    }

}
