package com.example.gateway.resolver;

import com.example.gateway.client.MockReviewClient;
import com.example.gateway.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewQueryResolver {

    private final MockReviewClient reviewClient;

    @QueryMapping
    public List<ReviewDto> reviewsByBook(@Argument String bookId) {
        return reviewClient.getApprovedReviewsByBookId(bookId);
    }
}
