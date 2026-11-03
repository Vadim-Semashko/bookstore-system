package com.example.analytics.repository;

import com.example.analytics.entity.BookStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookStatisticsRepository extends JpaRepository<BookStatistics, UUID> {
    List<BookStatistics> findAllByOrderByTotalSoldDesc();
}
