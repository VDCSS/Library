package com.example.demo.controller;

import com.example.demo.dto.LoanRequest;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public List<Loan> list() {
        return service.findAll();
    }

    @PostMapping
    public Loan createLoan(@RequestBody LoanRequest request) {
        return service.createLoan(request);
    }

    @PutMapping("/return/{id}")
    public void returnBook(@PathVariable Long id) {
        service.returnBook(id);
    }
}
