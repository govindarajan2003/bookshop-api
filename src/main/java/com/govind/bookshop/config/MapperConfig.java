package com.govind.bookshop.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central place to configure and expose a {@link ModelMapper} bean.
 *
 * <p>Uses the {@code LOOSE} strategy so fields with similar names can still be
 * mapped (e.g., {@code authorEntity} in JPA ↔ {@code author} in DTO). Switch to
 * {@code STRICT} if you prefer explicit, one-to-one property name matching.</p>
 */
@Configuration
public class MapperConfig {

    /**
     * Creates a configured {@link ModelMapper} bean for injection.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}