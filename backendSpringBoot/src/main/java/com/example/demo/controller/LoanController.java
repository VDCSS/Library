package com.example.demo.controller;

import com.example.demo.dto.CreateLoanDTO;
import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Loan>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateLoanDTO dto, Authentication auth) {
        Long personId = dto.getPersonId();
        // If not provided, resolve from token username
        if (personId == null) {
            String username = auth.getName();
            // find person by username
            // We'll need PersonRepository here, but to keep controller simple we'll assume admin provides personId,
            // or frontend will call /api/person/me to get own id and send it.
            return ResponseEntity.badRequest().body("personId is required for this endpoint");
        }
        Loan created = service.createLoan(personId, dto.getBookId(), dto.getDays());
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Long id) {
        Loan l = service.returnLoan(id);
        return ResponseEntity.ok(l);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Loan>> byPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(service.findByPerson(personId));
    }
}
