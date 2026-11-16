package com.example.gateway.resolver;

import com.example.gateway.client.MockCatalogClient;
import com.example.gateway.dto.AddBookInput;
import com.example.gateway.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CatalogMutationResolver {

    private final MockCatalogClient catalogClient;

    @MutationMapping
    public BookDto addBook(@Argument AddBookInput input) {
        return catalogClient.addBook(input);
    }

    @MutationMapping
    public BookDto updateBookPrice(@Argument String bookId, @Argument Double price) {
        return catalogClient.updateBookPrice(bookId, price);
    }

    @MutationMapping
    public BookDto restockBook(@Argument String bookId, @Argument Integer quantity) {
        return catalogClient.restockBook(bookId, quantity);
    }
}
