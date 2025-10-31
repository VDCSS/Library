package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    public void updateAvailability(Long id, boolean available) {
        Book book = findById(id);
        book.setAvailable(available);
        repository.save(book);
    }
}
