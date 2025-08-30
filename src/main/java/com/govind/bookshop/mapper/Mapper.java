package com.govind.bookshop.mapper;

/**
 * Simple bidirectional mapper contract between an entity (A) and a DTO (B).
 *
 * @param <A> entity type (JPA/persistence model)
 * @param <B> DTO type   (API contract)
 */
public interface Mapper<A, B> {

    /** Convert entity → DTO. */
    B toDto(A a);

    /** Convert DTO → entity. */
    A fromDto(B b);
}
