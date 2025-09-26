package com.example.demo.controller;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    public List<Book> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Book buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @GetMapping("/search")
    public List<Book> buscarPorFiltro(@RequestParam(required = false) String titulo,
                                       @RequestParam(required = false) String autor,
                                       @RequestParam(required = false) Boolean emprestado) {
        if (titulo != null) return repository.findByTituloContainingIgnoreCase(titulo);
        if (autor != null) return repository.findByAutorContainingIgnoreCase(autor);
        if (emprestado != null) return repository.findByEmprestado(emprestado);
        return repository.findAll();
    }

    @PostMapping
    public Book cadastrar(@Valid @RequestBody Book livro) {
        return repository.save(livro);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
