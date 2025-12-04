package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.*;
import com.example.demo.dto.BookDto;
import com.example.demo.mapper.DtoMapper;
import com.example.demo.model.Book;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookRepository bookRepo;
    private final ExemplarRepository exemplarRepo;
    public BookController(BookRepository br, ExemplarRepository er){this.bookRepo=br;this.exemplarRepo=er;}

    @GetMapping
    public List<BookDto> all(){
        return bookRepo.findAll().stream().map(b -> {
            int total = exemplarRepo.findByBookIdAndStatus(b.getId(), com.example.demo.model.ExemplarStatus.AVAILABLE).size()
                    + exemplarRepo.findByBookIdAndStatus(b.getId(), com.example.demo.model.ExemplarStatus.LOANED).size()
                    + exemplarRepo.findByBookIdAndStatus(b.getId(), com.example.demo.model.ExemplarStatus.RESERVED).size();
            int available = exemplarRepo.countByBookIdAndStatus(b.getId(), com.example.demo.model.ExemplarStatus.AVAILABLE) > 0 ?
                    (int)exemplarRepo.countByBookIdAndStatus(b.getId(), com.example.demo.model.ExemplarStatus.AVAILABLE) : 0;
            return DtoMapper.toBookDto(b, total, available);
        }).collect(Collectors.toList());
    }

    @PostMapping
    public Book create(@RequestBody Book b){ return bookRepo.save(b); }

    @PostMapping("/{bookId}/exemplar")
    public com.example.demo.dto.ExemplarDto addExemplar(@PathVariable Long bookId, @RequestBody com.example.demo.model.Exemplar ex) {
        Book b = bookRepo.findById(bookId).orElseThrow(() -> new com.example.demo.exception.NotFoundException("Book not found"));
        ex.setBook(b);
        ex.setStatus(com.example.demo.model.ExemplarStatus.AVAILABLE);
        com.example.demo.model.Exemplar saved = exemplarRepo.save(ex);
        return DtoMapper.toExDto(saved);
    }
}
