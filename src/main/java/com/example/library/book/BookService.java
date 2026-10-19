package com.example.library.book;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository books;

    public BookService(BookRepository books) {
        this.books = books;
    }

    public List<Book> findAll() {
        return books.findAll(Sort.by(Sort.Order.asc("title"), Sort.Order.asc("id")));
    }

    public Book findById(long id) {
        return books.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @Transactional
    public Book create(Book book) {
        return books.save(book);
    }

    @Transactional
    public void update(long id, Book changes) {
        Book existing = findById(id);
        existing.setTitle(changes.getTitle());
        existing.setAuthor(changes.getAuthor());
        existing.setPublicationYear(changes.getPublicationYear());
        existing.setStatus(changes.getStatus());
        // JPA writes these changes when the transaction commits.
    }

    @Transactional
    public void delete(long id) {
        books.delete(findById(id));
    }
}
