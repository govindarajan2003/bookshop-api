package com.govind.bookshop.book.domain.dto;

import com.govind.bookshop.author.domain.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API-facing representation of a Book.
 *
 * <p>Contains a nested {@link AuthorDto} to keep the payload cohesive
 * without leaking JPA internals.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    /** Natural identifier (primary key in the API). */
    private String isbn;

    /** Human-readable title. */
    private String title;

    /** Owning author (nullable). */
    private AuthorDto author;
}