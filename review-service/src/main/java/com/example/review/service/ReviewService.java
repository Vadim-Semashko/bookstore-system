package com.example.review.service;

import com.example.review.dto.ReviewDto;
import com.example.review.entity.Review;
import com.example.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
