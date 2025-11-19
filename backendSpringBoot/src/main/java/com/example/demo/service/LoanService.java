package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.model.Person;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final PersonRepository personRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo, PersonRepository personRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.personRepo = personRepo;
    }

    @Transactional
    public Loan createLoan(Long personId, Long bookId, int days) {
        Person p = personRepo.findById(personId).orElseThrow(() -> new IllegalArgumentException("Person not found"));
        // use pessimistic lock to avoid double-loan race conditions
        Book book = bookRepo.findByIdForUpdate(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getCopies() == null || book.getCopies() <= 0) {
            throw new IllegalStateException("No copies available");
        }

        book.setCopies(book.getCopies() - 1);
        book.setTimesBorrowed((book.getTimesBorrowed() == null ? 0 : book.getTimesBorrowed()) + 1);
        bookRepo.save(book);

        Loan loan = Loan.builder()
                .person(p)
                .book(book)
                .startDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(days))
                .status(LoanStatus.ACTIVE)
                .build();

        return loanRepo.save(loan);
    }

    @Transactional
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalStateException("Loan not active");
        }
        loan.setReturnedDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = bookRepo.findByIdForUpdate(loan.getBook().getId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setCopies(book.getCopies() + 1);
        bookRepo.save(book);

        return loanRepo.save(loan);
    }

    public List<Loan> findByPerson(Long personId) {
        return loanRepo.findByPersonId(personId);
    }
}
