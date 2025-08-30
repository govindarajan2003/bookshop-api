package com.govind.bookshop.author.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transport model exposed by the REST API for Author resources.
 *
 * <p>DTOs are used to shield clients from persistence details and to evolve
 * the API without breaking consumers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    /** Unique identifier of the author. */
    private Long id;

    /** Human-readable name of the author. */
    private String name;

    /** Age of the author. */
    private Integer age;
}