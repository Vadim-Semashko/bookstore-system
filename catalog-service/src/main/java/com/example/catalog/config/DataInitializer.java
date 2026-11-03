package com.example.catalog.config;

import com.example.catalog.entity.Author;
import com.example.catalog.entity.Book;
import com.example.catalog.entity.Category;
import com.example.catalog.repository.AuthorRepository;
import com.example.catalog.repository.BookRepository;
import com.example.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Bean
    CommandLineRunner initializeData() {
        return args -> {
            if (bookRepository.count() > 0) {
                return;
            }

            Author author1 = new Author();
            author1.setFirstName("Михаил");
            author1.setLastName("Булгаков");
            author1.setDescription("Русский писатель");
            authorRepository.save(author1);

            Author author2 = new Author();
            author2.setFirstName("Фёдор");
            author2.setLastName("Достоевский");
            author2.setDescription("Русский писатель");
            authorRepository.save(author2);

            Category category1 = new Category();
            category1.setName("Классическая литература");
            category1.setDescription("Произведения классической литературы");
            categoryRepository.save(category1);

            Category category2 = new Category();
            category2.setName("Романы");
            category2.setDescription("Художественные романы");
            categoryRepository.save(category2);

            Book book1 = new Book();
            book1.setTitle("Мастер и Маргарита");
            book1.setIsbn("9785000000001");
            book1.setPrice(new BigDecimal("35.50"));
            book1.setStockQuantity(15);
            book1.setRating(new BigDecimal("4.80"));
            book1.setAuthor(author1);
            book1.setCategory(category2);
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("Преступление и наказание");
            book2.setIsbn("9785000000002");
            book2.setPrice(new BigDecimal("28.00"));
            book2.setStockQuantity(10);
            book2.setRating(new BigDecimal("4.90"));
            book2.setAuthor(author2);
            book2.setCategory(category1);
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("Идиот");
            book3.setIsbn("9785000000003");
            book3.setPrice(new BigDecimal("32.00"));
            book3.setStockQuantity(8);
            book3.setRating(new BigDecimal("4.60"));
            book3.setAuthor(author2);
            book3.setCategory(category1);
            bookRepository.save(book3);
        };
    }
}
