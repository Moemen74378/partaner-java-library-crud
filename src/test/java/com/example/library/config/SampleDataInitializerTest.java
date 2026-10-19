package com.example.library.config;

import com.example.library.book.BookRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SampleDataInitializerTest {
    @Test
    void firstStartSeedsBooksAndPersistsMarker() {
        BookRepository books = mock(BookRepository.class);
        SeedMarkerRepository markers = mock(SeedMarkerRepository.class);
        when(markers.existsById("sample-books-v1")).thenReturn(false);

        new SampleDataInitializer(books, markers).run(null);

        verify(books).saveAll(anyList());
        verify(markers).save(any(SeedMarker.class));
    }

    @Test
    void initializedDatabaseDoesNotReseedEvenIfAllBooksWereDeleted() {
        BookRepository books = mock(BookRepository.class);
        SeedMarkerRepository markers = mock(SeedMarkerRepository.class);
        when(markers.existsById("sample-books-v1")).thenReturn(true);

        new SampleDataInitializer(books, markers).run(null);

        verify(books, never()).saveAll(anyList());
        verify(markers, never()).save(any(SeedMarker.class));
    }
}
