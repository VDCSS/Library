package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
}
