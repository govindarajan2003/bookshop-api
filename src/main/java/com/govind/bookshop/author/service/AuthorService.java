package com.govind.bookshop.author.service;

import com.govind.bookshop.author.domain.entity.AuthorEntity;

import java.util.List;
import java.util.Optional;

/**
 * Application service defining author-related operations.
 *
 * <p>This layer abstracts data access and centralizes business logic.
 */
public interface AuthorService {

    /**
     * Create or update an author (full save).
     */
    AuthorEntity save(AuthorEntity author);

    /**
     * Fetch all authors.
     */
    List<AuthorEntity> findAll();

    /**
     * Find one author by id.
     */
    Optional<AuthorEntity> findOne(Long id);

    /**
     * Check existence by id.
     */
    boolean isExists(Long id);

    /**
     * Apply non-null fields to the existing author with the given id.
     */
    AuthorEntity partialUpdate(Long id, AuthorEntity author);

    /**
     * Delete an author by id (idempotent).
     */
    void delete(Long id);
}