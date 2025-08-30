package com.govind.bookshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.govind.bookshop.MockDataUtil;
import com.govind.bookshop.author.domain.dto.AuthorDto;
import com.govind.bookshop.author.domain.entity.AuthorEntity;
import com.govind.bookshop.author.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Integration tests for the Author REST API using MockMvc.
 *
 * <p>Naming convention: <b>&lt;operation&gt;_should&lt;Expected&gt;_when&lt;Condition&gt;</b>
 * (underscores improve readability in Java identifiers).</p>
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorServiceImpl authorService;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorServiceImpl authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    // -------------------- CREATE --------------------

    @Test
    @DisplayName("POST /authors → returns saved author body")
    public void createAuthor_shouldReturnSavedAuthorBody_whenPayloadIsValid() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                        post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("govind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(23));
    }

    @Test
    @DisplayName("POST /authors → 201 Created on success")
    public void createAuthor_shouldReturn201Created_whenPayloadIsValid() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // -------------------- READ (LIST) --------------------

    @Test
    @DisplayName("GET /authors → 200 OK")
    public void listAuthors_shouldReturn200OK_whenCalled() throws Exception {
        mockMvc.perform(
                get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /authors → returns list of authors")
    public void listAuthors_shouldReturnList_whenAuthorsExist() throws Exception {
        // Arrange: seed one author
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        mockMvc.perform(
                        get("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("govind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(23));
    }

    // -------------------- READ (ONE) --------------------

    @Test
    @DisplayName("GET /authors/{id} → 200 OK when found")
    public void getAuthorById_shouldReturn200OK_whenAuthorExists() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        mockMvc.perform(
                get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /authors/{id} → 404 Not Found when missing")
    public void getAuthorById_shouldReturn404_whenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(
                get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("GET /authors/{id} → returns author body")
    public void getAuthorById_shouldReturnAuthorBody_whenAuthorExists() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        mockMvc.perform(
                        get("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("govind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(23));
    }

    // -------------------- UPDATE (PUT) --------------------

    @Test
    @DisplayName("PUT /authors/{id} → 404 when target does not exist")
    public void updateAuthor_shouldReturn404_whenAuthorDoesNotExist() throws Exception {
        AuthorDto authorDto = MockDataUtil.createMockAuthorDataADto();
        String jsonAuthor = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                put("/authors/999999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthor)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("PUT /authors/{id} → 200 OK when target exists")
    public void updateAuthor_shouldReturn200OK_whenAuthorExists() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        AuthorDto authorDto = MockDataUtil.createMockAuthorDataADto();
        String jsonAuthor = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthor)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PUT /authors/{id} → returns updated author body")
    public void updateAuthor_shouldReturnUpdatedAuthorBody_whenAuthorExists() throws Exception {
        // Arrange original entity
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        // New details for update
        AuthorEntity updated = MockDataUtil.createMockAuthorDataB();
        String jsonAuthor = objectMapper.writeValueAsString(updated);

        mockMvc.perform(
                        put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonAuthor)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("balaji"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(27));
    }

    // -------------------- PARTIAL UPDATE (PATCH) --------------------

    @Test
    @DisplayName("PATCH /authors/{id} → 200 OK")
    public void patchAuthor_shouldReturn200OK_whenPayloadIsValid() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        String jsonAuthor = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                patch("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthor)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PATCH /authors/{id} → updates a single field")
    public void patchAuthor_shouldUpdateSingleField_whenNameProvided() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        mockMvc.perform(
                        patch("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"baba\"}")
                ).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("baba"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(author.getAge()));
    }

    // -------------------- DELETE --------------------

    @Test
    @DisplayName("DELETE /authors/{id} → 204 No Content when existing")
    public void deleteAuthor_shouldReturn204NoContent_whenAuthorExists() throws Exception {
        AuthorEntity author = MockDataUtil.createMockAuthorDataA();
        authorService.save(author);

        mockMvc.perform(
                delete("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /authors/{id} → 204 No Content even when missing (idempotent)")
    public void deleteAuthor_shouldReturn204NoContent_whenAuthorDoesNotExist() throws Exception {
        // No need to seed – endpoint is idempotent by design
        mockMvc.perform(
                delete("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
