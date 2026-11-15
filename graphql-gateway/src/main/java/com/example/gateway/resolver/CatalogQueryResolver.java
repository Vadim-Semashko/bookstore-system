package com.example.gateway.resolver;

import com.example.gateway.client.MockCatalogClient;

import com.example.gateway.dto.AuthorDto;
import com.example.gateway.dto.BookDto;
import com.example.gateway.dto.BookFilterInput;
import com.example.gateway.dto.BookSortInput;
import com.example.gateway.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogQueryResolver {

    private final MockCatalogClient catalogClient;

    @QueryMapping
    public List<BookDto> books(
            @Argument BookFilterInput filter,
            @Argument BookSortInput sort
    ) {
        return catalogClient.getBooks(filter, sort);
    }

    @QueryMapping
    public BookDto book(@Argument String id) {
        return catalogClient.getBookById(id).orElse(null);
    }

    @QueryMapping
    public List<AuthorDto> authors() {
        return catalogClient.getAuthors();
    }

    @QueryMapping
    public List<CategoryDto> categories() {
        return catalogClient.getCategories();
    }
}
