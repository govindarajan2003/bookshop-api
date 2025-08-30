package com.govind.bookshop.mapper.impl;

import com.govind.bookshop.book.domain.dto.BookDto;
import com.govind.bookshop.book.domain.entity.BookEntity;
import com.govind.bookshop.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Maps between {@link BookEntity} and {@link BookDto}.
 *
 * <p>With {@code MatchingStrategies.LOOSE}, ModelMapper will map
 * {@code BookEntity.authorEntity} ↔ {@code BookDto.author} by type affinity and
 * similar naming. If you move to {@code STRICT}, add an explicit property map.</p>
 */
@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert JPA entity → API DTO (nested author included). */
    @Override
    public BookDto toDto(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    /** Convert API DTO → JPA entity (nested author included). */
    @Override
    public BookEntity fromDto(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}