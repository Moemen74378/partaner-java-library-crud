package com.example.library.config;

import java.util.List;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.book.BookStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = true)
public class SampleDataInitializer implements ApplicationRunner {
    private static final String SAMPLE_DATA_MARKER = "sample-books-v1";

    private final BookRepository books;
    private final SeedMarkerRepository markers;

    public SampleDataInitializer(BookRepository books, SeedMarkerRepository markers) {
        this.books = books;
        this.markers = markers;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (markers.existsById(SAMPLE_DATA_MARKER)) {
            return;
        }
        books.saveAll(List.of(
                new Book("Pride and Prejudice", "Jane Austen", 1813, BookStatus.AVAILABLE),
                new Book("The Time Machine", "H. G. Wells", 1895, BookStatus.BORROWED),
                new Book("Frankenstein", "Mary Shelley", 1818, BookStatus.AVAILABLE)
        ));
        markers.save(new SeedMarker(SAMPLE_DATA_MARKER));
    }
}
