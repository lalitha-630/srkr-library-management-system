package com.srkr.library.controller;

import com.srkr.library.model.Book;
import com.srkr.library.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String showBooks(HttpSession session, Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("registrationNumber",
                session.getAttribute("registrationNumber"));
        model.addAttribute("role", session.getAttribute("role"));

        return "books";
    }

    @GetMapping("/books/new")
    public String showBookForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute Book book, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        book.setAvailableCopies(book.getTotalCopies());
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        Book book = bookService.getBookById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid book ID: " + id));

        model.addAttribute("book", book);
        return "book-form";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/books";
        }

        bookService.deleteBook(id);
        return "redirect:/books";
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }
}