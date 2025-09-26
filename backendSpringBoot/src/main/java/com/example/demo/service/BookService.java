package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Page<BookDTO> listar(Pageable pageable) {
        return repo.findAll(pageable).map(BookMapper::toDTO);
    }

    public Page<BookDTO> buscar(String titulo, String autor, String emprestadoStr, Pageable pageable) {
        Boolean emprestado = null;
        if (emprestadoStr != null) {
            emprestado = Boolean.parseBoolean(emprestadoStr);
        }
        return repo.buscar(titulo, autor, emprestado, pageable).map(BookMapper::toDTO);
    }

    public BookDTO cadastrar(BookDTO dto) {
        Book livro = BookMapper.toEntity(dto);
        return BookMapper.toDTO(repo.save(livro));
    }

    public BookDTO atualizar(Long id, BookDTO dto) {
        Book livro = repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setEmprestado(dto.getEmprestado());
        livro.setEmprestadoPara(dto.getEmprestadoPara());
        return BookMapper.toDTO(repo.save(livro));
    }

    public void deletar(Long id) {
        if (!repo.existsById(id)) throw new BookNotFoundException(id);
        repo.deleteById(id);
    }
}
