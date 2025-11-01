package com.example.libraryapi.controller;

import com.example.libraryapi.model.Author;
import com.example.libraryapi.model.Book;
import com.example.libraryapi.repository.AuthorRepository;
import com.example.libraryapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;

    private Author author;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        author = new Author();
        author.setName("J. R. R. Tolkien");
        author = authorRepository.save(author);

        for (int i = 1; i <= 15; i++) {
            Book b = new Book();
            b.setTitle("Book " + (i < 10 ? "0" + i : i));
            b.setAuthor(author);
            bookRepository.save(b);
        }
    }

    @Test
    void getAllBooks_withPaginationAndSort_ok() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "title,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.content[0].title", startsWith("Book 15")));
    }

    @Test
    void getBookById_found_ok() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.author.name").value("J. R. R. Tolkien"));
    }

    @Test
    void getBookById_notFound_404() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));
    }

    @Test
    void createBook_valid_201() throws Exception {
        String body = """
            {
              "title": "The Hobbit",
              "genre": "fantasy",
              "yearPublished": 1937,
              "author": {"id": %d}
            }
            """.formatted(author.getId());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("The Hobbit"));
    }

    @Test
    void updateBook_ok() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();
        String body = """
            {
              "title":"Updated",
              "genre":"classic",
              "yearPublished":1950,
              "author":{"id":%d}
            }
            """.formatted(author.getId());

        mockMvc.perform(put("/api/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void deleteBook_noContent() throws Exception {
        Long id = bookRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isNotFound());
    }

}
