package com.example.demo.controller;

import com.example.demo.dto.LoanDTO;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/loans")

public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<LoanDTO> create(@Valid @RequestBody LoanDTO dto) {
        Long bookId = dto.getBookId();
        Long personId = dto.getPersonId();
        LocalDateTime loanDate = dto.getLoanDate();
        LocalDateTime returnDate = dto.getReturnDate();
        return ResponseEntity.ok(service.create(bookId, personId, loanDate, returnDate));
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnLoan(id));
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<LoanDTO>> byPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(service.findByPerson(personId));
    }
}
