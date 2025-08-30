package com.govind.bookshop.author.service.impl;

import com.govind.bookshop.author.service.AuthorService;
import com.govind.bookshop.author.domain.entity.AuthorEntity;
import com.govind.bookshop.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Default {@link AuthorService} implementation backed by Spring Data.
 *
 * <p>Only minimal logic lives here; complex rules should be added to this
 * layer rather than controllers or repositories.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity author) {
        // Ensure the patch is applied to the correct row
        author.setId(id);

        // Load, merge non-null fields, then persist
        return authorRepository.findById(id).map(existing -> {
            Optional.ofNullable(author.getAge()).ifPresent(existing::setAge);
            Optional.ofNullable(author.getName()).ifPresent(existing::setName);
            return authorRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Author not found for id=" + id)); // TODO: replace with domain-specific exception
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id); // idempotent
    }

    @Override
    public AuthorEntity save(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Override
    public List<AuthorEntity> findAll() {
        // Convert Iterable -> List to keep API ergonomic
        Iterable<AuthorEntity> result = authorRepository.findAll();
        return StreamSupport.stream(result.spliterator(), false).toList();
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }
}