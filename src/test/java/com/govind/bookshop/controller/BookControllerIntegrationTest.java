package com.govind.bookshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govind.bookshop.MockDataUtil;
import com.govind.bookshop.book.domain.dto.BookDto;
import com.govind.bookshop.book.domain.entity.BookEntity;
import com.govind.bookshop.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Integration tests for the Book REST API using MockMvc.
 *
 * <p>Naming convention: <b>&lt;operation&gt;_should&lt;Expected&gt;_when&lt;Condition&gt;</b>
 * (underscores improve readability in Java identifiers).</p>
 */
@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookServiceImpl bookService;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, BookServiceImpl bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    // -------------------- CREATE/UPDATE via PUT --------------------

    @Test
    @DisplayName("PUT /books/{isbn} → 201 Created when new book is created")
    public void createOrUpdateBook_shouldReturn201Created_whenNewBookIsPut() throws Exception {
        BookDto bookDtoA = MockDataUtil.createMockBookDtoData1(null);
        String bookJson = objectMapper.writeValueAsString(bookDtoA);

        mockMvc.perform(
                put("/books/" + bookDtoA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("PUT /books/{isbn} → returns saved book body when created")
    public void createOrUpdateBook_shouldReturnSavedBookBody_whenNewBookIsPut() throws Exception {
        BookDto book = MockDataUtil.createMockBookDtoData1(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                        put("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    @DisplayName("PUT /books/{isbn} → 201 Created when target ISBN not found")
    public void createOrUpdateBook_shouldReturn201Created_whenIsbnNotFound() throws Exception {
        BookEntity book = MockDataUtil.createMockBookData2(null);
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                put("/books/120-2093-33-213")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("PUT /books/{isbn} → 200 OK when updating existing book")
    public void createOrUpdateBook_shouldReturn200OK_whenBookExists() throws Exception {
        BookEntity book0 = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book0.getIsbn(), book0);

        String jsonBook = objectMapper.writeValueAsString(book0);

        mockMvc.perform(
                put("/books/123-123-145-675")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /books/{isbn} → returns updated book body")
    public void createOrUpdateBook_shouldReturnUpdatedBookBody_whenBookExists() throws Exception {
        BookEntity book0 = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book0.getIsbn(), book0);

        BookEntity book = MockDataUtil.createMockBookData2(null); // new title, same isbn path below
        String jsonBook = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                        put("/books/123-123-145-675")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBook)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book0.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // -------------------- LIST (GET /books) --------------------

    @Test
    @DisplayName("GET /books → 200 OK")
    public void listBooks_shouldReturn200OK_whenCalled() throws Exception {
        mockMvc.perform(
                get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /books → returns paged list when books exist")
    public void listBooks_shouldReturnPagedList_whenBooksExist() throws Exception {
        BookEntity book = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(book.getTitle()));
    }

    // -------------------- READ ONE (GET /books/{isbn}) --------------------

    @Test
    @DisplayName("GET /books/{isbn} → 200 OK when found")
    public void getBookByIsbn_shouldReturn200OK_whenBookExists() throws Exception {
        BookEntity book = MockDataUtil.createMockBookData2(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                get("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /books/{isbn} → 404 Not Found when missing")
    public void getBookByIsbn_shouldReturn404NotFound_whenBookMissing() throws Exception {
        mockMvc.perform(
                get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("GET /books/{isbn} → returns book body")
    public void getBookByIsbn_shouldReturnBookBody_whenBookExists() throws Exception {
        BookEntity book = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book.getIsbn(), book);

        mockMvc.perform(
                        get("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    // -------------------- PARTIAL UPDATE (PATCH) --------------------

    @Test
    @DisplayName("PATCH /books/{isbn} → 200 OK and returns current values when same payload provided")
    public void patchBook_shouldReturn200OKAndCurrentValues_whenSamePayloadProvided() throws Exception {
        BookEntity book0 = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book0.getIsbn(), book0);

        String jsonBook = objectMapper.writeValueAsString(book0);

        mockMvc.perform(
                        patch("/books/" + book0.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBook)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book0.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book0.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PATCH /books/{isbn} → updates specific fields")
    public void patchBook_shouldUpdateFields_whenPartialPayloadProvided() throws Exception {
        BookEntity book0 = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book0.getIsbn(), book0);

        BookEntity mockBook = MockDataUtil.createMockBookData2(null);
        mockBook.setIsbn(book0.getIsbn());
        mockBook.setTitle("the food"); // change only title

        String jsonBook = objectMapper.writeValueAsString(mockBook);

        mockMvc.perform(
                        patch("/books/" + book0.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBook)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book0.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockBook.getTitle()));
    }

    // -------------------- DELETE --------------------

    @Test
    @DisplayName("DELETE /books/{isbn} → 204 No Content when existing")
    public void deleteBook_shouldReturn204NoContent_whenBookExists() throws Exception {
        BookEntity book0 = MockDataUtil.createMockBookData1(null);
        bookService.createUpdateBook(book0.getIsbn(), book0);

        mockMvc.perform(
                delete("/books/" + book0.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /books/{isbn} → 204 No Content even when missing (idempotent)")
    public void deleteBook_shouldReturn204NoContent_whenBookDoesNotExist() throws Exception {
        mockMvc.perform(
                delete("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
