package com.govind.bookshop.author.controller;

import com.govind.bookshop.author.domain.dto.AuthorDto;
import com.govind.bookshop.author.domain.entity.AuthorEntity;
import com.govind.bookshop.mapper.Mapper;
import com.govind.bookshop.author.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller exposing CRUD endpoints for {@link AuthorEntity}.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>POST   /authors                – create author</li>
 *   <li>GET    /authors                – list authors</li>
 *   <li>GET    /authors/{id}           – get author by id</li>
 *   <li>PUT    /authors/{id}           – full update (upsert not allowed)</li>
 *   <li>PATCH  /authors/{id}           – partial update</li>
 *   <li>DELETE /authors/{id}           – delete author</li>
 * </ul>
 *
 * <p>All responses use DTOs to decouple the API surface from persistence.
 */
@RestController
public class AuthorController {

    /** Domain service containing business logic and data access. */
    private final AuthorService authorService;

    /** Generic mapper for converting between entity and DTO types. */
    private final Mapper<AuthorEntity, AuthorDto> mapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> mapper) {
        this.authorService = authorService;
        this.mapper = mapper;
    }

    /**
     * Create a new author.
     *
     * @param author payload (name, age); id is ignored if present
     * @return created author with generated id and HTTP 201
     */
    @PostMapping("/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity toSave = mapper.fromDto(author);
        AuthorEntity saved = authorService.save(toSave);
        // Map the SAVED entity so generated fields (e.g. id) are returned to the client.
        AuthorDto body = mapper.toDto(saved);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    /**
     * List all authors.
     *
     * @return collection of authors (200 OK)
     */
    @GetMapping("/authors")
    public List<AuthorDto> listAuthors() {
        List<AuthorEntity> result = authorService.findAll();
        return result.stream().map(mapper::toDto).toList();
    }

    /**
     * Fetch a single author by id.
     *
     * @param id author id
     * @return 200 with author if found, otherwise 404
     */
    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> displayOneAuthor(@PathVariable("id") Long id) {
        Optional<AuthorEntity> result = authorService.findOne(id);
        return result
                .map(entity -> new ResponseEntity<>(mapper.toDto(entity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Full update of an existing author. This is not an upsert.
     *
     * @param id        author id to update
     * @param authorDto new state for the resource
     * @return 200 with updated author, or 404 if the id does not exist
     */
    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable("id") Long id,
                                                  @RequestBody AuthorDto authorDto) {
        if (!authorService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorDto.setId(id);
        AuthorEntity saved = authorService.save(mapper.fromDto(authorDto));
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.OK);
    }

    /**
     * Partial update of an existing author. Only non-null fields are applied.
     *
     * @param id        author id
     * @param authorDto patch payload (nulls are ignored)
     * @return 200 with updated author, or 404 if not found
     */
    @PatchMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(@PathVariable("id") Long id,
                                                   @RequestBody AuthorDto authorDto) {
        if (!authorService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity patched = authorService.partialUpdate(id, mapper.fromDto(authorDto));
        return new ResponseEntity<>(mapper.toDto(patched), HttpStatus.OK);
    }

    /**
     * Delete an author by id.
     *
     * @param id author id
     * @return 204 No Content (idempotent)
     */
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable("id") Long id) {
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}