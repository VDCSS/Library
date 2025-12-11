package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.mapper.DtoMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Exemplar;
import com.example.demo.model.ExemplarStatus;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ExemplarRepository;
import com.example.demo.service.BookService;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final ExemplarRepository exemplarRepo;

    public BookServiceImpl(BookRepository bookRepo, ExemplarRepository exemplarRepo) {
        this.bookRepo = bookRepo;
        this.exemplarRepo = exemplarRepo;
    }

    @Override
    public Book create(Book book) {
        return bookRepo.save(book);
    }

    @Override
    @Transactional
    public Book update(Long id, Book book) {
        Book existing = bookRepo.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setYear(book.getYear());
        existing.setCategory(book.getCategory());
        return bookRepo.save(existing);
    }

    @Override
    public Book findById(Long id) {
        return bookRepo.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public List<Book> findAll() { return bookRepo.findAll(); }

    @Override
    @Transactional
    public Exemplar addExemplar(Long bookId, Exemplar exemplar) {
        Book b = bookRepo.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found"));
        exemplar.setBook(b);
        exemplar.setStatus(ExemplarStatus.AVAILABLE);
        return exemplarRepo.save(exemplar);
    }

    @Override
    public List<Exemplar> findExemplarsByBook(Long bookId) {
        return exemplarRepo.findByBookIdAndStatus(bookId, ExemplarStatus.AVAILABLE);
    }

    @Override
    public BookDto toDto(Book book) {
        int total = exemplarRepo.findByBookIdAndStatus(book.getId(), ExemplarStatus.AVAILABLE).size()
                + exemplarRepo.findByBookIdAndStatus(book.getId(), ExemplarStatus.LOANED).size()
                + exemplarRepo.findByBookIdAndStatus(book.getId(), ExemplarStatus.RESERVED).size();
        int available = (int) exemplarRepo.countByBookIdAndStatus(book.getId(), ExemplarStatus.AVAILABLE);
        return DtoMapper.toBookDto(book, total, available);
    }
}
