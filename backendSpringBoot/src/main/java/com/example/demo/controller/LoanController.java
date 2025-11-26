package com.example.demo.controller;

import com.example.demo.dto.CreateLoanDTO;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService svc;

    public LoanController(LoanService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<Loan>> all() { return ResponseEntity.ok(svc.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> get(@PathVariable Long id) { return ResponseEntity.ok(svc.findById(id)); }

    @PostMapping
    public ResponseEntity<Loan> create(@Valid @RequestBody CreateLoanDTO dto) {
        if (dto.getPersonId() == null) throw new IllegalArgumentException("personId required");
        Loan l = svc.createLoan(dto.getPersonId(), dto.getBookId(), dto.getDays());
        return ResponseEntity.ok(l);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        return ResponseEntity.ok(svc.returnLoan(id));
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Loan>> byPerson(@PathVariable Long personId) { return ResponseEntity.ok(svc.findByPerson(personId)); }
}
