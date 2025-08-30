package com.govind.bookshop;

import com.govind.bookshop.author.domain.dto.AuthorDto;
import com.govind.bookshop.author.domain.entity.AuthorEntity;
import com.govind.bookshop.book.domain.dto.BookDto;
import com.govind.bookshop.book.domain.entity.BookEntity;

/**
 * Test fixtures for Authors and Books.
 *
 * <p>Prefer the {@code sample*} methods. The older {@code createMock*} methods are
 * kept as deprecated aliases to avoid breaking existing tests and will be removed later.</p>
 */
public final class MockDataUtil {

    private MockDataUtil() {
        // Utility class – no instances
    }

    // ---------------------------------------------------------------------
    // Preferred "sample*" factory methods
    // ---------------------------------------------------------------------

    /** Sample AuthorEntity A: name=govind, age=23. */
    public static AuthorEntity sampleAuthorEntityA() {
        return AuthorEntity.builder()
                .name("govind")
                .age(23)
                .build();
    }

    /** Sample AuthorDto A: name=govind, age=23. */
    public static AuthorDto sampleAuthorDtoA() {
        return AuthorDto.builder()
                .name("govind")
                .age(23)
                .build();
    }

    /** Sample AuthorEntity B: name=balaji, age=27. */
    public static AuthorEntity sampleAuthorEntityB() {
        return AuthorEntity.builder()
                .name("balaji")
                .age(27)
                .build();
    }

    /** Sample AuthorEntity C: name=gard, age=29. */
    public static AuthorEntity sampleAuthorEntityC() {
        return AuthorEntity.builder()
                .name("gard")
                .age(29)
                .build();
    }

    /**
     * Sample BookEntity #1: isbn=123-123-145-675, title="The one".
     * @param authorEntity optional linked author (nullable)
     */
    public static BookEntity sampleBookEntity1(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-123-145-675")
                .title("The one")
                .authorEntity(authorEntity)
                .build();
    }

    /**
     * Sample BookDto #1: isbn=123-123-145-675, title="The one".
     * @param authorDto optional linked author (nullable)
     */
    public static BookDto sampleBookDto1(final AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("123-123-145-675")
                .title("The one")
                .author(authorDto)
                .build();
    }

    /**
     * Sample BookEntity #2: isbn=124-124-144-674, title="The second".
     * @param authorEntity optional linked author (nullable)
     */
    public static BookEntity sampleBookEntity2(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("124-124-144-674")
                .title("The second")
                .authorEntity(authorEntity)
                .build();
    }

    /**
     * Sample BookEntity #3: isbn=123-123-143-673, title="The third".
     * @param authorEntity optional linked author (nullable)
     */
    public static BookEntity sampleBookEntity3(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-123-143-673")
                .title("The third")
                .authorEntity(authorEntity)
                .build();
    }

    // ---------------------------------------------------------------------
    // Deprecated legacy aliases (for backward compatibility)
    // ---------------------------------------------------------------------

    /** @deprecated Use {@link #sampleAuthorEntityA()} instead. */
    @Deprecated
    public static AuthorEntity createMockAuthorDataA() {
        return sampleAuthorEntityA();
    }

    /** @deprecated Use {@link #sampleAuthorDtoA()} instead. */
    @Deprecated
    public static AuthorDto createMockAuthorDataADto() {
        return sampleAuthorDtoA();
    }

    /** @deprecated Use {@link #sampleAuthorEntityB()} instead. */
    @Deprecated
    public static AuthorEntity createMockAuthorDataB() {
        return sampleAuthorEntityB();
    }

    /** @deprecated Use {@link #sampleAuthorEntityC()} instead. */
    @Deprecated
    public static AuthorEntity createMockAuthorDataC() {
        return sampleAuthorEntityC();
    }

    /** @deprecated Use {@link #sampleBookEntity1(AuthorEntity)} instead. */
    @Deprecated
    public static BookEntity createMockBookData1(final AuthorEntity authorEntity) {
        return sampleBookEntity1(authorEntity);
    }

    /** @deprecated Use {@link #sampleBookDto1(AuthorDto)} instead. */
    @Deprecated
    public static BookDto createMockBookDtoData1(final AuthorDto authorDto) {
        return sampleBookDto1(authorDto);
    }

    /** @deprecated Use {@link #sampleBookEntity2(AuthorEntity)} instead. */
    @Deprecated
    public static BookEntity createMockBookData2(final AuthorEntity authorEntity) {
        return sampleBookEntity2(authorEntity);
    }

    /** @deprecated Use {@link #sampleBookEntity3(AuthorEntity)} instead. */
    @Deprecated
    public static BookEntity createMockBookData3(final AuthorEntity authorEntity) {
        return sampleBookEntity3(authorEntity);
    }
}
