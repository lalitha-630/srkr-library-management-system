package com.srkr.library.controller;

import com.srkr.library.model.Book;
import com.srkr.library.model.Loan;
import com.srkr.library.model.Student;
import com.srkr.library.repository.BookRepository;
import com.srkr.library.repository.LoanRepository;
import com.srkr.library.service.LoanService;
import com.srkr.library.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class LoanController {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final StudentService studentService;
    private final LoanService loanService;

    public LoanController(
            BookRepository bookRepository,
            LoanRepository loanRepository,
            StudentService studentService,
            LoanService loanService) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.studentService = studentService;
        this.loanService = loanService;
    }

    @PostMapping("/requests/{bookId}")
    public String requestBook(
            @PathVariable Long bookId,
            HttpSession session) {

        if (!"STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        String registrationNumber =
                (String) session.getAttribute("registrationNumber");

        if (registrationNumber == null) {
            return "redirect:/login";
        }

        Student student = studentService
                .findByRegistrationNumber(registrationNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            return "redirect:/books?unavailable";
        }

        Loan loan = new Loan();
        loan.setStudent(student);
        loan.setBook(book);
        loan.setStatus("PENDING");

        loanRepository.save(loan);

        return "redirect:/books?requested";
    }

    @PostMapping("/admin/requests/{requestId}/approve")
    public String approveRequest(
            @PathVariable Long requestId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate issueDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDate,
            HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        loanService.approveRequest(requestId, issueDate, dueDate);
        return "redirect:/";
    }

    @PostMapping("/admin/requests/{requestId}/decline")
    public String declineRequest(
            @PathVariable Long requestId,
            HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        loanService.declineRequest(requestId);
        return "redirect:/";
    }
}