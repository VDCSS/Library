package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.Exemplar;
import java.util.List;

public interface BookService {
    Book create(Book book);
    Book update(Long id, Book book);
    Book findById(Long id);
    List<Book> findAll();
    Exemplar addExemplar(Long bookId, Exemplar exemplar);
    List<Exemplar> findExemplarsByBook(Long bookId);
    BookDto toDto(Book book);
}
