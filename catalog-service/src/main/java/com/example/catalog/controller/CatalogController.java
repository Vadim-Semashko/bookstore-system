package com.example.catalog.controller;

import com.example.catalog.dto.AddBookRequest;
import com.example.catalog.dto.AuthorDto;
import com.example.catalog.dto.BookDto;
import com.example.catalog.dto.CategoryDto;
import com.example.catalog.dto.RestockBookRequest;
import com.example.catalog.dto.UpdateBookPriceRequest;
import com.example.catalog.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return catalogService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBook(
            @PathVariable UUID id
    ) {
        return ResponseEntity.of(
                catalogService.getBookById(id)
        );
    }

    @GetMapping("/authors")
    public List<AuthorDto> getAuthors() {
        return catalogService.getAllAuthors();
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return catalogService.getAllCategories();
    }


    @PostMapping("/books")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody AddBookRequest request) {
        return ResponseEntity.status(201).body(catalogService.addBook(request));
    }

    @PatchMapping("/books/{id}/price")
    public BookDto updateBookPrice(@PathVariable UUID id, @Valid @RequestBody UpdateBookPriceRequest request) {
        return catalogService.updateBookPrice(id, request.price());
    }

    @PostMapping("/books/{id}/restock")
    public BookDto restockBook(@PathVariable UUID id, @Valid @RequestBody RestockBookRequest request) {
        return catalogService.restockBook(id, request.quantity());
    }

}
