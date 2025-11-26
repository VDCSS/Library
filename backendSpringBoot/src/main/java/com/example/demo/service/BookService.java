package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) { this.repo = repo; }

    public List<Book> all() { return repo.findAll(); }
    public Book get(Long id) { return repo.findById(id).orElseThrow(); }
    public Book save(Book b) {
        if (b.getAvailableQuantity() == null) b.setAvailableQuantity(b.getTotalQuantity());
        return repo.save(b);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
