package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService svc;

    public BookController(BookService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<Book>> all() { return ResponseEntity.ok(svc.all()); }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable Long id) { return ResponseEntity.ok(svc.get(id)); }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book b) { return ResponseEntity.ok(svc.save(b)); }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book payload) {
        Book existing = svc.get(id);
        existing.setTitle(payload.getTitle()); existing.setAuthor(payload.getAuthor());
        existing.setIsbn(payload.getIsbn()); existing.setTotalQuantity(payload.getTotalQuantity());
        existing.setAvailableQuantity(payload.getAvailableQuantity());
        return ResponseEntity.ok(svc.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) { svc.delete(id); return ResponseEntity.noContent().build(); }
}
