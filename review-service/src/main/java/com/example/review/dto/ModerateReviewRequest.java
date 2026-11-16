package com.example.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ModerateReviewRequest(
        @NotBlank String decision
) {}
