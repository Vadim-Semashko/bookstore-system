package com.example.analytics.repository;

import com.example.analytics.entity.AuthorStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorStatisticsRepository extends JpaRepository<AuthorStatistics, UUID> {
    List<AuthorStatistics> findAllByOrderByTotalSoldDesc();
}
