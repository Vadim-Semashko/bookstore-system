package com.example.gateway.client;

import com.example.gateway.dto.AddReviewInput;
import com.example.gateway.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MockReviewClient {

    private final MockCatalogClient catalogClient;

    private final List<ReviewDto> reviews = new ArrayList<>(List.of(
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
    ));

    public List<ReviewDto> getApprovedReviewsByBookId(String bookId) {
        return reviews.stream()
                .filter(review -> review.bookId().equals(bookId))
                .filter(review -> "APPROVED".equals(review.status()))
                .toList();
    }


    public synchronized ReviewDto addReview(String clientId, AddReviewInput input) {
        if (clientId == null || clientId.isBlank() ||
                input == null ||
                input.bookId() == null || input.bookId().isBlank() ||
                input.rating() == null ||
                input.rating() < 1 || input.rating() > 5 ||
                input.text() == null || input.text().isBlank()) {
            throw new IllegalArgumentException("Некорректные данные отзыва");
        }

        if (catalogClient.getBookById(input.bookId()).isEmpty()) {
            throw new IllegalArgumentException("Книга не найдена");
        }

        boolean alreadyReviewed = reviews.stream()
                .anyMatch(review ->
                        review.clientId().equals(clientId) && review.bookId().equals(input.bookId())
                );

        if (alreadyReviewed) {
            throw new IllegalStateException("Вы уже оставили отзыв на эту книгу");
        }

        ReviewDto review;
        review = new ReviewDto(
                UUID.randomUUID().toString(),
                clientId,
                input.bookId(),
                input.rating(),
                input.text(),
                "PENDING",
                LocalDateTime.now().toString()
        );

        reviews.add(review);
        return review;
    }

    public synchronized ReviewDto moderateReview(String reviewId, String decision) {
        if (!"APPROVED".equals(decision) && !"REJECTED".equals(decision)) {
            throw new IllegalArgumentException("Недопустимое решение модерации");
        }

        ReviewDto oldReview = reviews.stream()
                .filter(review -> review.id().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Отзыв не найден"));

        if (!"PENDING".equals(oldReview.status())) {
            throw new IllegalStateException("Можно модерировать только ожидающий отзыв");
        }

        ReviewDto updatedReview = new ReviewDto(
                oldReview.id(),
                oldReview.clientId(),
                oldReview.bookId(),
                oldReview.rating(),
                oldReview.text(),
                decision,
                oldReview.createdAt()
        );

        reviews.replaceAll(review ->
                review.id().equals(reviewId) ? updatedReview : review
        );

        return updatedReview;
    }


}
