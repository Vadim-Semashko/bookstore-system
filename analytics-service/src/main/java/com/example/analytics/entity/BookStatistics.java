package com.example.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "book_statistics")
@Getter
@Setter
@NoArgsConstructor
public class BookStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "book_id", nullable = false, unique = true)
    private UUID bookId;

    @Column(name = "total_sold", nullable = false)
    private Integer totalSold;

    @Column(name = "total_revenue", nullable = false,
            precision = 14, scale = 2)
    private BigDecimal totalRevenue;
}
