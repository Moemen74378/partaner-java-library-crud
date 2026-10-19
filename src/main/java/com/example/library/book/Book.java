package com.example.library.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Enter a title.")
    @Size(max = 120, message = "Use 120 characters or fewer.")
    @Column(nullable = false, length = 120)
    private String title;

    @NotBlank(message = "Enter an author.")
    @Size(max = 120, message = "Use 120 characters or fewer.")
    @Column(nullable = false, length = 120)
    private String author;

    @NotNull(message = "Enter a publication year.")
    @Min(value = 1, message = "The year must be between 1 and 2100.")
    @Max(value = 2100, message = "The year must be between 1 and 2100.")
    @Column(nullable = false)
    private Integer publicationYear;

    @NotNull(message = "Choose a status.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status = BookStatus.AVAILABLE;

    public Book() {
        // Required by JPA and Spring form binding.
    }

    public Book(String title, String author, Integer publicationYear, BookStatus status) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
