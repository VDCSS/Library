package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<Book>> all() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Long id) {
        return ResponseEntity.ok(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found")));
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book b) {
        if (b.getAvailableQuantity() == null) b.setAvailableQuantity(b.getTotalQuantity());
        return ResponseEntity.ok(repo.save(b));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book payload) {
        Book existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        existing.setTitle(payload.getTitle());
        existing.setAuthor(payload.getAuthor());
        existing.setIsbn(payload.getIsbn());
        existing.setTotalQuantity(payload.getTotalQuantity());
        existing.setAvailableQuantity(payload.getAvailableQuantity());
        return ResponseEntity.ok(repo.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
