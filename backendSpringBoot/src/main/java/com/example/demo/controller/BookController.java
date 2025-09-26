package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public Page<BookDTO> listar(Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/buscar")
    public Page<BookDTO> buscar(@RequestParam(required = false) String titulo,
                                 @RequestParam(required = false) String autor,
                                 @RequestParam(required = false) String emprestado,
                                 Pageable pageable) {
        return service.buscar(titulo, autor, emprestado, pageable);
    }

    @PostMapping
    public ResponseEntity<BookDTO> cadastrar(@Valid @RequestBody BookDTO dto) {
        BookDTO salvo = service.cadastrar(dto);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> atualizar(@PathVariable Long id, @Valid @RequestBody BookDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
