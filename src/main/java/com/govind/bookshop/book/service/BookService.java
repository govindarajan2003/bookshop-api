package com.govind.bookshop.book.service;

import com.govind.bookshop.book.domain.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Application service contract for book operations.
 */
public interface BookService {

    /**
     * Create or replace a book under the given ISBN.
     */
    BookEntity createUpdateBook(String isbn, BookEntity book);

    /**
     * List all books (non-paged).
     */
    List<BookEntity> findAll();

    /**
     * List books with paging/sorting.
     */
    Page<BookEntity> findAll(Pageable pageable);

    /**
     * Find a single book by ISBN.
     */
    Optional<BookEntity> findOne(String isbn);

    /**
     * Check if a book exists by ISBN.
     */
    boolean isExists(String isbn);

    /**
     * Apply a partial update to the book with the given ISBN.
     */
    BookEntity partialUpdate(String isbn, BookEntity book);

    /**
     * Delete a book by ISBN.
     */
    void delete(String isbn);
}