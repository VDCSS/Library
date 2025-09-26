package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;

public class BookMapper {

    public static BookDTO toDTO(Book livro) {
        return new BookDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getEmprestado(),
                livro.getEmprestadoPara()
        );
    }

    public static Book toEntity(BookDTO dto) {
        Book livro = new Book();
        livro.setId(dto.getId());
        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setEmprestado(dto.getEmprestado());
        livro.setEmprestadoPara(dto.getEmprestadoPara());
        return livro;
    }
}
