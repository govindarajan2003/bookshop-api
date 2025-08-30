package com.govind.bookshop.book.domain.entity;

import com.govind.bookshop.author.domain.entity.AuthorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA entity mapped to the {@code books} table.
 *
 * <p>Use {@link com.govind.bookshop.book.domain.dto.BookDto} for API exposure.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class BookEntity {

    /** Primary key: ISBN. */
    @Id
    private String isbn;

    /** Book title. */
    private String title;

    /**
     * Owning side of the many-to-one relationship to Author.
     * <p>Note: {@code CascadeType.ALL} means author changes will cascade from book operations.
     * Ensure this is desired; in many designs, cascade on ManyToOne is omitted.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;
}