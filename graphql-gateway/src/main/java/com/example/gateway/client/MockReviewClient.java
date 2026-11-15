package com.example.gateway.client;

import com.example.gateway.dto.ReviewDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockReviewClient {

    private final List<ReviewDto> reviews = List.of(
            new ReviewDto(
                    "r1",
                    "660e8400-e29b-41d4-a716-446655440000",
                    "b1",
                    5,
                    "Отличная книга, рекомендую!",
                    "APPROVED",
                    "2026-11-01T14:30:00"
            ),
            new ReviewDto(
                    "r2",
                    "660e8400-e29b-41d4-a716-446655440000",
                    "b2",
                    4,
                    "Интересное произведение.",
                    "APPROVED",
                    "2026-11-02T10:15:00"
            )
    );

    public List<ReviewDto> getApprovedReviewsByBookId(String bookId) {
        return reviews.stream()
                .filter(review -> review.bookId().equals(bookId))
                .filter(review -> "APPROVED".equals(review.status()))
                .toList();
    }
}
