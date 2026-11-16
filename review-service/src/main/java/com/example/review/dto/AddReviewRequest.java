package com.example.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddReviewRequest(
        @NotNull UUID clientId,
        @NotNull UUID bookId,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank String text
) {}
