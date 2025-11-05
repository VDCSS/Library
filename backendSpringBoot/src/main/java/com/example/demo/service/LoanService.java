package com.example.demo.service;

import com.example.demo.dto.LoanDTO;
import com.example.demo.mapper.LoanMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.model.Person;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.events.LoanCreatedEvent;
import com.example.demo.events.LoanReturnedEvent;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final PersonRepository personRepo;
    private final LoanMapper mapper;
    private final ApplicationEventPublisher publisher;

    public LoanService(LoanRepository loanRepo,
                       BookRepository bookRepo,
                       PersonRepository personRepo,
                       LoanMapper mapper,
                       ApplicationEventPublisher publisher) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.personRepo = personRepo;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    public List<LoanDTO> findAll() {
        return loanRepo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public LoanDTO findById(Long id) {
        Loan l = loanRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
        return mapper.toDTO(l);
    }

    @Transactional
    public LoanDTO create(Long bookId, Long personId, LocalDateTime loanDate, LocalDateTime returnDate) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
        Person person = personRepo.findById(personId).orElseThrow(() -> new ResourceNotFoundException("Person not found: " + personId));

        if (!book.getAvailable()) throw new IllegalStateException("Book is not available");

        book.setAvailable(false);
        book.setTimesBorrowed((book.getTimesBorrowed() == null ? 1 : book.getTimesBorrowed() + 1));
        bookRepo.save(book);

        Loan loan = Loan.builder()
                .book(book)
                .person(person)
                .loanDate(loanDate != null ? loanDate : LocalDateTime.now())
                .returnDate(returnDate)
                .status(LoanStatus.ACTIVE)
                .build();

        Loan saved = loanRepo.save(loan);
        publisher.publishEvent(new LoanCreatedEvent(this, saved));
        return mapper.toDTO(saved);
    }

    @Transactional
    public LoanDTO returnLoan(Long id) {
        Loan loan = loanRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
        if (loan.getStatus() == LoanStatus.RETURNED) return mapper.toDTO(loan);
        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);
        loanRepo.save(loan);
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepo.save(book);
        publisher.publishEvent(new LoanReturnedEvent(this, loan));
        return mapper.toDTO(loan);
    }

    public List<LoanDTO> findByPerson(Long personId) {
        return loanRepo.findByPersonId(personId).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<LoanDTO> findOverdue() {
        return loanRepo.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.OVERDUE)
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
