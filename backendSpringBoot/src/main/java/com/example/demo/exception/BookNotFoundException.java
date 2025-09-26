package com.example.demo.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Livro não encontrado com ID: " + id);
    }
}
