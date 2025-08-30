package com.govind.bookshop.book.controller;

import com.govind.bookshop.book.domain.dto.BookDto;
import com.govind.bookshop.book.domain.entity.BookEntity;
import com.govind.bookshop.mapper.Mapper;
import com.govind.bookshop.book.service.impl.BookServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for {@link BookEntity} resources.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>PUT    /books/{isbn}   – create or replace a book (idempotent by ISBN)</li>
 *   <li>GET    /books          – list (paged)</li>
 *   <li>GET    /books/{isbn}   – fetch one</li>
 *   <li>PATCH  /books/{isbn}   – partial update</li>
 *   <li>DELETE /books/{isbn}   – delete</li>
 * </ul>
 *
 * <p>All inputs/outputs use {@link BookDto} to decouple persistence from the API shape.
 */
@RestController
public class BookController {

    /** Application service handling business logic and data access. */
    private final BookServiceImpl bookService;

    /** Generic mapper between entity and DTO. */
    private final Mapper<BookEntity, BookDto> mapper;

    public BookController(BookServiceImpl bookService, Mapper<BookEntity, BookDto> mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    /**
     * Create or fully update a book identified by its ISBN.
     *
     * @param isbn  natural identifier of the book
     * @param book  request payload
     * @return 201 (created) if it did not exist, or 200 (ok) if it was replaced
     */
    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn,
                                                    @RequestBody BookDto book) {
        BookEntity toSave = mapper.fromDto(book);
        boolean exists = bookService.isExists(isbn);

        BookEntity saved = bookService.createUpdateBook(isbn, toSave);
        BookDto body = mapper.toDto(saved);

        return new ResponseEntity<>(body, exists ? HttpStatus.OK : HttpStatus.CREATED);
    }

    /**
     * Return a paginated list of books.
     *
     * @param pageable Spring Data pagination & sorting
     * @return a page of {@link BookDto}
     */
    @GetMapping("/books")
    public Page<BookDto> listBooks(Pageable pageable) {
        Page<BookEntity> page = bookService.findAll(pageable);
        return page.map(mapper::toDto);
    }

    /**
     * Fetch a single book by ISBN.
     *
     * @param isbn the ISBN
     * @return 200 with body if found, else 404
     */
    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> displayOneBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> found = bookService.findOne(isbn);
        return found
                .map(entity -> new ResponseEntity<>(mapper.toDto(entity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Apply a partial update to a book. Only non-null fields are merged.
     *
     * @param isbn    target book
     * @param bookDto patch payload
     * @return 200 with the updated book, or 404 if the ISBN does not exist
     */
    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateOneBook(@PathVariable("isbn") String isbn,
                                                        @RequestBody BookDto bookDto) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity patched = bookService.partialUpdate(isbn, mapper.fromDto(bookDto));
        return new ResponseEntity<>(mapper.toDto(patched), HttpStatus.OK);
    }

    /**
     * Delete a book by ISBN. Idempotent.
     *
     * @param isbn ISBN to delete
     * @return 204 No Content
     */
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable("isbn") String isbn) {
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}