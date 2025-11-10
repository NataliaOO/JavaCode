package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Book> bookRowMapper;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookRowMapper = (rs, rowNum) -> {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublicationYear(rs.getInt("publicationYear"));
            return book;
        };
    }

    public void save(Book book) {
        String sql = "INSERT INTO book (title, author, publicationYear) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        List<Book> books = jdbcTemplate.query(sql, new Object[]{id}, bookRowMapper);

        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM book";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public Book update(Long id, Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, publicationYear = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), id);
        return book;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM book WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
