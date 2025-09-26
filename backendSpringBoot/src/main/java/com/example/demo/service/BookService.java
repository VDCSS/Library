package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Page<BookDTO> listar(Pageable pageable) {
        return repo.findAll(pageable).map(BookMapper::toDTO);
    }

    public Page<BookDTO> buscar(String titulo, String autor, String emprestado, Pageable pageable) {
        return repo.buscar(titulo, autor, emprestado, pageable).map(BookMapper::toDTO);
    }

    public Optional<BookDTO> buscarPorId(Long id) {
        return repo.findById(id).map(BookMapper::toDTO);
    }

    public BookDTO salvar(BookDTO dto) {
        Book livro = BookMapper.toEntity(dto);
        return BookMapper.toDTO(repo.save(livro));
    }

    public Optional<BookDTO> atualizar(Long id, BookDTO dto) {
        return repo.findById(id).map(existing -> {
            existing.setTitulo(dto.getTitulo());
            existing.setAutor(dto.getAutor());
            existing.setEmprestadoPara(dto.getEmprestadoPara());
            return BookMapper.toDTO(repo.save(existing));
        });
    }

    public boolean excluir(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}