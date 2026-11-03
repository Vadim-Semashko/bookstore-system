package com.example.analytics.config;

import com.example.analytics.entity.AuthorStatistics;
import com.example.analytics.entity.BookStatistics;
import com.example.analytics.entity.SalesStatistics;

import com.example.analytics.repository.AuthorStatisticsRepository;
import com.example.analytics.repository.BookStatisticsRepository;
import com.example.analytics.repository.SalesStatisticsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeStatistics(
            SalesStatisticsRepository salesRepository,
            BookStatisticsRepository bookRepository,
            AuthorStatisticsRepository authorRepository
    ) {
        return args -> {
            if (salesRepository.count() == 0) {
                SalesStatistics sales = new SalesStatistics();
                sales.setPeriodDate(LocalDate.now());
                sales.setTotalOrders(5);
                sales.setTotalRevenue(new BigDecimal("495.00"));
                salesRepository.save(sales);
            }

            if (bookRepository.count() == 0) {
                BookStatistics book = new BookStatistics();

                book.setBookId(UUID.fromString(
                        "ae54e3af-3ef5-4353-80cf-69538de56718"
                ));

                book.setTotalSold(8);
                book.setTotalRevenue(new BigDecimal("284.00"));
                bookRepository.save(book);
            }

            if (authorRepository.count() == 0) {
                AuthorStatistics author = new AuthorStatistics();

                author.setAuthorId(UUID.fromString(
                        "a081c1ad-3ebc-4985-b8e4-c41cebee471c"
                ));

                author.setTotalSold(12);
                author.setTotalRevenue(new BigDecimal("420.00"));
                authorRepository.save(author);
            }
        };
    }
}
