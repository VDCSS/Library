package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.LoanDTO;
import com.example.demo.service.BookService;
import com.example.demo.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final LoanService loanService;
    private final BookService bookService;

    public ReportController(LoanService loanService, BookService bookService) {
        this.loanService = loanService;
        this.bookService = bookService;
    }

    @GetMapping("/active-loans")
    public ResponseEntity<List<LoanDTO>> activeLoans() {
        return ResponseEntity.ok(loanService.findAll().stream()
                .filter(l -> "ACTIVE".equals(l.getStatus()))
                .toList());
    }

    @GetMapping("/overdue-loans")
    public ResponseEntity<List<LoanDTO>> overdueLoans() {
        return ResponseEntity.ok(loanService.findOverdue());
    }

    @GetMapping("/most-borrowed")
    public ResponseEntity<List<BookDTO>> mostBorrowed() {
        return ResponseEntity.ok(bookService.findAll().stream()
                .sorted((a,b) -> b.getTimesBorrowed().compareTo(a.getTimesBorrowed()))
                .toList());
    }
}
