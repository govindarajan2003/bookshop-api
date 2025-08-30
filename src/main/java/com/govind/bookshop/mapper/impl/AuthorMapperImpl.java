package com.govind.bookshop.mapper.impl;

import com.govind.bookshop.author.domain.dto.AuthorDto;
import com.govind.bookshop.author.domain.entity.AuthorEntity;
import com.govind.bookshop.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Maps between {@link AuthorEntity} (persistence model) and {@link AuthorDto} (API model).
 *
 * <p>Relies on a single, shared {@link ModelMapper} bean configured in {@link com.govind.bookshop.config.MapperConfig}.</p>
 */
@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, AuthorDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /** Convert JPA entity → API DTO. */
    @Override
    public AuthorDto toDto(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, AuthorDto.class);
    }

    /** Convert API DTO → JPA entity. */
    @Override
    public AuthorEntity fromDto(AuthorDto authorDto) {
        return modelMapper.map(authorDto, AuthorEntity.class);
    }
}
