package com.example.review.controller;

import com.example.review.dto.ReviewDto;
import com.example.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDto> getReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable UUID id
    ) {
        return ResponseEntity.of(
                reviewService.getReviewById(id)
        );
    }

    @GetMapping("/book/{bookId}")
    public List<ReviewDto> getBookReviews(
            @PathVariable UUID bookId
    ) {
        return reviewService.getApprovedReviewsByBookId(bookId);
    }
}
