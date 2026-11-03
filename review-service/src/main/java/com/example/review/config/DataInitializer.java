package com.example.review.config;

import com.example.review.entity.Review;
import com.example.review.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeReviews(ReviewRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Review review = new Review();

            review.setClientId(
                    UUID.fromString("ea890473-ba51-4bcf-8cc3-fdd9cf2479ab")
            );

            review.setBookId(
                    UUID.fromString("ae54e3af-3ef5-4353-80cf-69538de56718")
            );

            review.setRating(5);
            review.setText("Отличная книга, рекомендую!");
            review.setStatus("APPROVED");
            review.setCreatedAt(LocalDateTime.now());

            repository.save(review);
        };
    }
}
