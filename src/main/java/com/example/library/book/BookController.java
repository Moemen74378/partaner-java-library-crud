package com.example.library.book;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {
    private final BookService books;

    public BookController(BookService books) {
        this.books = books;
    }

    @InitBinder("book")
    void configureBookBinding(WebDataBinder binder) {
        binder.setAllowedFields("title", "author", "publicationYear", "status");
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute("statuses")
    public BookStatus[] statuses() {
        return BookStatus.values();
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String list(Model model) {
        List<Book> allBooks = books.findAll();
        long available = allBooks.stream().filter(book -> book.getStatus() == BookStatus.AVAILABLE).count();
        model.addAttribute("books", allBooks);
        model.addAttribute("totalBooks", allBooks.size());
        model.addAttribute("availableBooks", available);
        model.addAttribute("borrowedBooks", allBooks.size() - available);
        return "books/list";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("editMode", false);
        return "books/form";
    }

    @PostMapping("/books")
    public String create(@Valid @ModelAttribute("book") Book book, BindingResult errors,
                         Model model, RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            model.addAttribute("editMode", false);
            return "books/form";
        }
        books.create(book);
        redirect.addFlashAttribute("message", "Book added to your library.");
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("book", books.findById(id));
        model.addAttribute("editMode", true);
        return "books/form";
    }

    @PostMapping("/books/{id}")
    public String update(@PathVariable long id, @Valid @ModelAttribute("book") Book book,
                         BindingResult errors, Model model, RedirectAttributes redirect) {
        // Check existence even when submitted fields are invalid.
        Book existing = books.findById(id);
        // The ID always comes from the route; submitted ID fields are not bound.
        book.setId(existing.getId());
        if (errors.hasErrors()) {
            model.addAttribute("editMode", true);
            return "books/form";
        }
        books.update(id, book);
        redirect.addFlashAttribute("message", "Book updated successfully.");
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirect) {
        books.delete(id);
        redirect.addFlashAttribute("message", "Book deleted from your library.");
        return "redirect:/books";
    }
}
