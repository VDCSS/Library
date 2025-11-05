package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository repo;
    private final BookMapper mapper;

    public BookService(BookRepository repo, BookMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<BookDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public BookDTO findById(Long id) {
        Book b = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
        return mapper.toDTO(b);
    }

    @Transactional
    public BookDTO create(BookDTO dto) {
        Book entity = mapper.toEntity(dto);
        if (entity.getAddedDate() == null) entity.setAddedDate(java.time.LocalDate.now());
        if (entity.getAvailable() == null) entity.setAvailable(true);
        Book saved = repo.save(entity);
        return mapper.toDTO(saved);
    }

    @Transactional
    public BookDTO update(Long id, BookDTO dto) {
        Book existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
        existing.setTitle(dto.getTitle());
        existing.setAuthor(dto.getAuthor());
        existing.setGenre(dto.getGenre());
        existing.setQuantity(dto.getQuantity());
        existing.setAddedDate(dto.getAddedDate());
        existing.setAvailable(dto.getAvailable());
        Book saved = repo.save(existing);
        return mapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Book not found: " + id);
        repo.deleteById(id);
    }

    @Transactional
    public void incrementTimesBorrowed(Book book) {
        book.setTimesBorrowed((book.getTimesBorrowed() == null ? 1 : book.getTimesBorrowed() + 1));
        repo.save(book);
    }
}
