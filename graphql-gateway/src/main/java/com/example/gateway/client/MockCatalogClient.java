package com.example.gateway.client;

import com.example.gateway.dto.AuthorDto;
import com.example.gateway.dto.BookDto;
import com.example.gateway.dto.BookFilterInput;
import com.example.gateway.dto.BookSortInput;
import com.example.gateway.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MockCatalogClient {

    private final AuthorDto bulgakov = new AuthorDto(
            "a1", "Михаил", "Булгаков", "Русский писатель"
    );

    private final AuthorDto dostoevsky = new AuthorDto(
            "a2", "Фёдор", "Достоевский", "Русский писатель"
    );

    private final CategoryDto classics = new CategoryDto(
            "c1", "Классическая литература",
            "Произведения классической литературы"
    );

    private final CategoryDto novels = new CategoryDto(
            "c2", "Романы", "Художественные романы"
    );

    private final List<BookDto> books = List.of(
            new BookDto(
                    "b1", "Мастер и Маргарита",
                    "9785000000001", 35.50, 15, 4.8,
                    bulgakov, novels
            ),
            new BookDto(
                    "b2", "Преступление и наказание",
                    "9785000000002", 28.00, 10, 4.9,
                    dostoevsky, classics
            ),
            new BookDto(
                    "b3", "Идиот",
                    "9785000000003", 32.00, 8, 4.6,
                    dostoevsky, classics
            )
    );


    public List<BookDto> getBooks(
            BookFilterInput filter,
            BookSortInput sort
    ) {
        Stream<BookDto> stream = books.stream();

        if (filter != null) {
            if (filter.title() != null && !filter.title().isBlank()) {
                String title = filter.title().toLowerCase(Locale.ROOT);
                stream = stream.filter(book ->
                        book.title().toLowerCase(Locale.ROOT).contains(title)
                );
            }

            if (filter.authorId() != null) {
                stream = stream.filter(book ->
                        book.author().id().equals(filter.authorId())
                );
            }

            if (filter.categoryId() != null) {
                stream = stream.filter(book ->
                        book.category().id().equals(filter.categoryId())
                );
            }

            if (filter.minPrice() != null) {
                stream = stream.filter(book ->
                        book.price() >= filter.minPrice()
                );
            }

            if (filter.maxPrice() != null) {
                stream = stream.filter(book ->
                        book.price() <= filter.maxPrice()
                );
            }
        }

        if (sort != null) {
            Comparator<BookDto> comparator = switch (sort.field()) {
                case "TITLE" -> Comparator.comparing(
                        BookDto::title,
                        String.CASE_INSENSITIVE_ORDER
                );
                case "PRICE" -> Comparator.comparing(BookDto::price);
                case "RATING" -> Comparator.comparing(
                        BookDto::rating,
                        Comparator.nullsLast(Double::compareTo)
                );
                default -> throw new IllegalArgumentException(
                        "Unsupported sort field: " + sort.field()
                );
            };

            if ("DESC".equals(sort.direction())) {
                comparator = comparator.reversed();
            }

            stream = stream.sorted(comparator);
        }

        return stream.toList();
    }

    public Optional<BookDto> getBookById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst();
    }

    public List<AuthorDto> getAuthors() {
        return List.of(bulgakov, dostoevsky);
    }

    public List<CategoryDto> getCategories() {
        return List.of(classics, novels);
    }
}
