package com.govind.bookshop.book.repository;

import com.govind.bookshop.book.domain.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data repository for {@link BookEntity}.
 *
 * <p>Exposes CRUD, paging, and sorting. (Note: {@code PagingAndSortingRepository}
 * already extends {@code CrudRepository}; both are included here to match the current setup.)
 */
public interface BookRepository extends CrudRepository<BookEntity, String>,
        PagingAndSortingRepository<BookEntity, String> {
}