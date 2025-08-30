package com.govind.bookshop.book.service.impl;

import com.govind.bookshop.book.domain.entity.BookEntity;
import com.govind.bookshop.book.repository.BookRepository;
import com.govind.bookshop.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Default {@link BookService} implementation using Spring Data.
 */
@Component
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity book) {
        // Ensure the path-variable ISBN is authoritative
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    @Override
    public List<BookEntity> findAll() {
        Iterable<BookEntity> all = bookRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).toList();
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn); // idempotent
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity book) {
        // Ensure we patch the correct row
        book.setIsbn(isbn);

        return bookRepository.findById(isbn).map(existing -> {
            Optional.ofNullable(book.getTitle()).ifPresent(existing::setTitle);
            Optional.ofNullable(book.getAuthorEntity()).ifPresent(existing::setAuthorEntity);
            return bookRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Book not found for isbn=" + isbn)); // TODO: replace with domain-specific exception
    }
}