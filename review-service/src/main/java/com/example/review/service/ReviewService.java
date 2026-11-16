package com.example.review.service;

import com.example.review.dto.AddReviewRequest;
import com.example.review.dto.ReviewDto;
import com.example.review.entity.Review;
import com.example.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ReviewDto> getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .map(this::toDto);
    }

    public List<ReviewDto> getApprovedReviewsByBookId(UUID bookId) {
        return reviewRepository
                .findByBookIdAndStatus(bookId, "APPROVED")
                .stream()
                .map(this::toDto)
                .toList();
    }


    @Transactional
    public ReviewDto addReview(AddReviewRequest request) {

        if (reviewRepository.existsByClientIdAndBookId(request.clientId(), request.bookId())) {
            throw new IllegalStateException("Вы уже оставили отзыв на эту книгу");
        }

        Review review = new Review();
        review.setClientId(request.clientId());
        review.setBookId(request.bookId());
        review.setRating(request.rating());
        review.setText(request.text());
        review.setStatus("PENDING");
        review.setCreatedAt(LocalDateTime.now());

        return toDto(reviewRepository.save(review));
    }

    @Transactional
    public ReviewDto moderateReview(UUID reviewId, String decision) {

        if (!"APPROVED".equals(decision) && !"REJECTED".equals(decision)) {
            throw new IllegalArgumentException("Решение должно быть APPROVED или REJECTED");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Отзыв не найден"));

        if (!"PENDING".equals(review.getStatus())) {
            throw new IllegalStateException("Можно модерировать только ожидающий отзыв");
        }

        review.setStatus(decision);

        return toDto(reviewRepository.save(review));
    }

    private ReviewDto toDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getClientId(),
                review.getBookId(),
                review.getRating(),
                review.getText(),
                review.getStatus(),
                review.getCreatedAt()
        );
    }
}
