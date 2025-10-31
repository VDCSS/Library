package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookMapper::toDTO)
                .orElse(null);
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = BookMapper.toEntity(bookDTO);
        Book saved = bookRepository.save(book);
        return BookMapper.toDTO(saved);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(bookDTO.getTitle());
                    existing.setAuthor(bookDTO.getAuthor());
                    existing.setGenre(bookDTO.getGenre());
                    existing.setQuantity(bookDTO.getQuantity());
                    existing.setAddedDate(bookDTO.getAddedDate());
                    existing.setAvailable(bookDTO.isAvailable());
                    return BookMapper.toDTO(bookRepository.save(existing));
                })
                .orElse(null);
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
