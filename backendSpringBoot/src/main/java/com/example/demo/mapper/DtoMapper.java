package com.example.demo.mapper;
import com.example.demo.dto.*;
import com.example.demo.model.*;
import java.util.Optional;
public class DtoMapper {
    public static BookDto toBookDto(Book b, int total, int available){
        return BookDto.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).isbn(b.getIsbn()).year(b.getYear()).category(b.getCategory()).totalExemplars(total).availableExemplars(available).build();
    }
    public static ExemplarDto toExDto(Exemplar e){
        return ExemplarDto.builder().id(e.getId()).bookId(Optional.ofNullable(e.getBook()).map(Book::getId).orElse(null)).barcode(e.getBarcode()).status(e.getStatus().name()).build();
    }
    public static LoanDto toLoanDto(Loan l){
        return LoanDto.builder().id(l.getId()).userId(Optional.ofNullable(l.getUser()).map(User::getId).orElse(null)).exemplarId(Optional.ofNullable(l.getExemplar()).map(Exemplar::getId).orElse(null)).loanDate(l.getLoanDate()).expectedReturnDate(l.getExpectedReturnDate()).realReturnDate(l.getRealReturnDate()).renewalsRemaining(l.getRenewalsRemaining()).renewalsDone(l.getRenewalsDone()).returned(l.getReturned()).build();
    }
}
