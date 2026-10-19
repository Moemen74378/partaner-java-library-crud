package com.example.library;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.book.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:library-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.seed-data=false"
})
@AutoConfigureMockMvc
@Transactional
class BookCrudIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository books;

    @Test
    void homeRedirectsToBooks() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void listRendersStoredBooksAndCorrectCounts() throws Exception {
        books.save(new Book("Pride and Prejudice", "Jane Austen", 1813, BookStatus.AVAILABLE));
        books.save(new Book("The Time Machine", "H. G. Wells", 1895, BookStatus.BORROWED));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("totalBooks", 2))
                .andExpect(model().attribute("availableBooks", 1L))
                .andExpect(model().attribute("borrowedBooks", 1L))
                .andExpect(content().string(containsString("Pride and Prejudice")))
                .andExpect(content().string(containsString("Jane Austen")));
    }

    @Test
    void createFormRendersAndCreatePersistsTrimmedInput() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attribute("editMode", false))
                .andExpect(content().string(containsString("name=\"title\"")));

        mvc.perform(post("/books")
                        .param("title", "  A Study in Scarlet  ")
                        .param("author", "  Arthur Conan Doyle  ")
                        .param("publicationYear", "1887")
                        .param("status", "AVAILABLE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("message"));

        assertThat(books.findAll()).singleElement().satisfies(book -> {
            assertThat(book.getId()).isNotNull();
            assertThat(book.getTitle()).isEqualTo("A Study in Scarlet");
            assertThat(book.getAuthor()).isEqualTo("Arthur Conan Doyle");
            assertThat(book.getPublicationYear()).isEqualTo(1887);
            assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        });
    }

    @Test
    void invalidCreateRendersFieldErrorsWithoutWriting() throws Exception {
        mvc.perform(post("/books")
                        .param("title", "   ")
                        .param("author", "x".repeat(121))
                        .param("publicationYear", "2101")
                        .param("status", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attributeHasFieldErrors("book", "title", "author", "publicationYear", "status"))
                .andExpect(content().string(containsString("Enter a title.")));

        assertThat(books.count()).isZero();
    }

    @Test
    void nonNumericYearAndUnknownStatusAreValidationErrors() throws Exception {
        mvc.perform(post("/books")
                        .param("title", "Valid title")
                        .param("author", "Valid author")
                        .param("publicationYear", "not-a-year")
                        .param("status", "UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("book", "publicationYear", "status"));

        assertThat(books.count()).isZero();
    }

    @Test
    void editFormRendersAndUpdateChangesExistingBook() throws Exception {
        Book original = books.save(new Book("Old title", "Old author", 1900, BookStatus.AVAILABLE));

        mvc.perform(get("/books/{id}/edit", original.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attribute("editMode", true))
                .andExpect(content().string(containsString("Old title")));

        mvc.perform(post("/books/{id}", original.getId())
                        .param("title", "Updated title")
                        .param("author", "Updated author")
                        .param("publicationYear", "1950")
                        .param("status", "BORROWED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        assertThat(books.count()).isEqualTo(1);
        Book updated = books.findById(original.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("Updated title");
        assertThat(updated.getAuthor()).isEqualTo("Updated author");
        assertThat(updated.getPublicationYear()).isEqualTo(1950);
        assertThat(updated.getStatus()).isEqualTo(BookStatus.BORROWED);
    }

    @Test
    void invalidUpdateRetainsCorrectFormActionAndOriginalBook() throws Exception {
        Book original = books.save(new Book("Keep this title", "Author", 1900, BookStatus.AVAILABLE));

        mvc.perform(post("/books/{id}", original.getId())
                        .param("title", "")
                        .param("author", "Author")
                        .param("publicationYear", "0")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("book", "title", "publicationYear"))
                .andExpect(model().attribute("editMode", true))
                .andExpect(content().string(containsString("action=\"/books/" + original.getId() + "\"")));

        assertThat(books.findById(original.getId()).orElseThrow().getTitle()).isEqualTo("Keep this title");
    }

    @Test
    void deleteRemovesBookAndGetCannotDeleteIt() throws Exception {
        Book book = books.save(new Book("Remove me", "Author", 1900, BookStatus.AVAILABLE));

        mvc.perform(get("/books/{id}/delete", book.getId()))
                .andExpect(status().isMethodNotAllowed());
        assertThat(books.existsById(book.getId())).isTrue();

        mvc.perform(post("/books/{id}/delete", book.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("message"));
        assertThat(books.existsById(book.getId())).isFalse();
    }

    @Test
    void missingBooksReturn404ForEditUpdateAndDelete() throws Exception {
        mvc.perform(get("/books/999999/edit")).andExpect(status().isNotFound());
        mvc.perform(post("/books/999999")
                        .param("title", "Valid title")
                        .param("author", "Author")
                        .param("publicationYear", "1900")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isNotFound());
        mvc.perform(post("/books/999999")
                        .param("title", ""))
                .andExpect(status().isNotFound());
        mvc.perform(post("/books/999999/delete")).andExpect(status().isNotFound());
        assertThat(books.count()).isZero();
    }

    @Test
    void submittedIdCannotOverwriteAnotherBookDuringCreate() throws Exception {
        Book original = books.save(new Book("Original", "Author", 1900, BookStatus.AVAILABLE));

        mvc.perform(post("/books")
                        .param("id", original.getId().toString())
                        .param("title", "Separate book")
                        .param("author", "Author")
                        .param("publicationYear", "1950")
                        .param("status", "BORROWED"))
                .andExpect(status().is3xxRedirection());

        assertThat(books.count()).isEqualTo(2);
        assertThat(books.findById(original.getId()).orElseThrow().getTitle()).isEqualTo("Original");
    }
}
