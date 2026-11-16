package com.example.gateway.client;

import com.example.gateway.dto.AddBookInput;
import com.example.gateway.dto.AuthorDto;
import com.example.gateway.dto.BookDto;
import com.example.gateway.dto.BookFilterInput;
import com.example.gateway.dto.BookSortInput;
import com.example.gateway.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class MockCatalogClient {

    private final Map<String, Map<String, Integer>> reservations = new HashMap<>();

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

    private final List<BookDto> books = new ArrayList<>(List.of(
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
    ));


    public synchronized List<BookDto> getBooks(
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

    public synchronized Optional<BookDto> getBookById(String id) {
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

    public synchronized BookDto addBook(AddBookInput input) {
        if (input == null ||
                input.title() == null || input.title().isBlank() ||
                input.isbn() == null || input.isbn().isBlank() ||
                input.authorId() == null ||
                input.categoryId() == null ||
                input.price() == null || input.price() <= 0 ||
                input.stockQuantity() == null ||
                input.stockQuantity() < 0) {
            throw new IllegalArgumentException("Некорректные данные книги");
        }

        boolean isbnExists = books.stream()
                .anyMatch(book -> book.isbn().equals(input.isbn()));

        if (isbnExists) {
            throw new IllegalArgumentException("Книга с таким ISBN уже существует");
        }

        AuthorDto author = getAuthors().stream()
                .filter(a -> a.id().equals(input.authorId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Автор не найден"));

        CategoryDto category = getCategories().stream()
                .filter(c -> c.id().equals(input.categoryId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));

        BookDto book = new BookDto(
                UUID.randomUUID().toString(),
                input.title(),
                input.isbn(),
                input.price(),
                input.stockQuantity(),
                null,
                author,
                category
        );

        books.add(book);
        return book;
    }

    public synchronized BookDto updateBookPrice(String bookId, Double price) {
        if (price == null || !Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше нуля");
        }

        BookDto oldBook = getBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));

        BookDto updatedBook = new BookDto(
                oldBook.id(),
                oldBook.title(),
                oldBook.isbn(),
                price,
                oldBook.stockQuantity(),
                oldBook.rating(),
                oldBook.author(),
                oldBook.category()
        );

        books.replaceAll(book ->
                book.id().equals(bookId) ? updatedBook : book
        );

        return updatedBook;
    }

    public synchronized BookDto restockBook(String bookId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        BookDto oldBook = getBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));

        int newStock = Math.addExact(oldBook.stockQuantity(), quantity);

        BookDto updatedBook = new BookDto(
                oldBook.id(),
                oldBook.title(),
                oldBook.isbn(),
                oldBook.price(),
                newStock,
                oldBook.rating(),
                oldBook.author(),
                oldBook.category()
        );

        books.replaceAll(book ->
                book.id().equals(bookId) ? updatedBook : book
        );

        return updatedBook;
    }

    public synchronized Double reserveBook(
            String orderId, String bookId, int quantity
    ) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }

        BookDto book = getBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));

        if (book.stockQuantity() < quantity) {
            throw new IllegalStateException("Недостаточно экземпляров книги");
        }

        BookDto updated = new BookDto(
                book.id(),
                book.title(),
                book.isbn(),
                book.price(),
                book.stockQuantity() - quantity,
                book.rating(),
                book.author(),
                book.category()
        );

        books.replaceAll(b ->
                b.id().equals(bookId) ? updated : b
        );

        reservations
                .computeIfAbsent(orderId, id -> new HashMap<>())
                .merge(bookId, quantity, Integer::sum);

        return book.price();
    }

    public synchronized void releaseReservation(String orderId) {
        Map<String, Integer> reserved = reservations.remove(orderId);

        if (reserved == null) {
            return;
        }

        reserved.forEach((bookId, quantity) -> {
            BookDto book = getBookById(bookId)
                    .orElseThrow(() -> new IllegalStateException("Зарезервированная книга не найдена"));

            BookDto updated = new BookDto(
                    book.id(),
                    book.title(),
                    book.isbn(),
                    book.price(),
                    Math.addExact(book.stockQuantity(), quantity),
                    book.rating(),
                    book.author(),
                    book.category()
            );

            books.replaceAll(b ->
                    b.id().equals(bookId) ? updated : b
            );
        });
    }

    public synchronized boolean hasReservation(String orderId) {
        return reservations.containsKey(orderId);
    }

    public synchronized void confirmReservation(String orderId) {

        if (!reservations.containsKey(orderId)) {
            throw new IllegalStateException("Резервирование для заказа не найдено");
        }

        reservations.remove(orderId);
    }

}
