package com.example.catalog.service;

import com.example.catalog.dto.AuthorDto;
import com.example.catalog.dto.BookDto;
import com.example.catalog.dto.CategoryDto;
import com.example.catalog.entity.Author;
import com.example.catalog.entity.Book;
import com.example.catalog.entity.Category;
import com.example.catalog.repository.AuthorRepository;
import com.example.catalog.repository.BookRepository;
import com.example.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toBookDto)
                .toList();
    }

    public Optional<BookDto> getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(this::toBookDto);
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::toAuthorDto)
                .toList();
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toCategoryDto)
                .toList();
    }

    private BookDto toBookDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getStockQuantity(),
                book.getRating(),
                toAuthorDto(book.getAuthor()),
                toCategoryDto(book.getCategory())
        );
    }

    private AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getDescription()
        );
    }

    private CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
