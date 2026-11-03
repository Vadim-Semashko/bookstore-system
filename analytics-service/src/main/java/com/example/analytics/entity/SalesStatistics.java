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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sales_statistics")
@Getter
@Setter
@NoArgsConstructor
public class SalesStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "period_date", nullable = false, unique = true)
    private LocalDate periodDate;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @Column(name = "total_revenue", nullable = false,
            precision = 14, scale = 2)
    private BigDecimal totalRevenue;
}
