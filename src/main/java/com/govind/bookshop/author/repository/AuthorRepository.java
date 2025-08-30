package com.govind.bookshop.author.repository;

import com.govind.bookshop.author.domain.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data repository for {@link AuthorEntity}.
 *
 * <p>Exposes basic CRUD operations. For paging/sorting, consider extending
 * {@code PagingAndSortingRepository} or {@code JpaRepository}.
 */
public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {
}