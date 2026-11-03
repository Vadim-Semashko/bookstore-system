package com.example.analytics.repository;

import com.example.analytics.entity.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, UUID> {
    List<SalesStatistics> findByPeriodDateBetweenOrderByPeriodDateAsc(LocalDate dateFrom, LocalDate dateTo);
}
