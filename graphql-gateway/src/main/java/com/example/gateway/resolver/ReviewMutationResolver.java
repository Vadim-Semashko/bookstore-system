package com.example.gateway.resolver;

import com.example.gateway.client.MockOrderClient;
import com.example.gateway.client.MockReviewClient;
import com.example.gateway.dto.AddReviewInput;
import com.example.gateway.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewMutationResolver {

    private final MockReviewClient reviewClient;
    private final MockOrderClient orderClient;

    @MutationMapping
    public ReviewDto addReview(@Argument("input") AddReviewInput input) {
        return reviewClient.addReview(orderClient.getTestClientId(), input);
    }

    @MutationMapping
    public ReviewDto moderateReview(@Argument String reviewId, @Argument String decision) {
        return reviewClient.moderateReview(reviewId, decision);
    }
}
