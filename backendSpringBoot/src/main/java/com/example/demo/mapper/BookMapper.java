package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        if (book == null) return null;
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getQuantity(),
                book.getAddedDate(),
                book.isAvailable()
        );
    }

    public static Book toEntity(BookDTO dto) {
        if (dto == null) return null;
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .quantity(dto.getQuantity())
                .addedDate(dto.getAddedDate())
                .available(dto.isAvailable())
                .build();
    }
}
