package com.example.demo.mapper;

import com.example.demo.dto.LoanDTO;
import com.example.demo.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookMapper.class, PersonMapper.class})
public interface LoanMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "person.name", target = "personName")
    @Mapping(source = "person.email", target = "personEmail")
    @Mapping(source = "person.phone", target = "personPhone")
    @Mapping(source = "loanDate", target = "loanDate")
    @Mapping(source = "returnDate", target = "returnDate")
    @Mapping(source = "status", target = "status")
    LoanDTO toDTO(Loan loan);
}
