package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final NotificationRepository notificationRepo;
    private final PersonRepository personRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo,
                       NotificationRepository notificationRepo, PersonRepository personRepo) {
        this.loanRepo = loanRepo; this.bookRepo = bookRepo; this.notificationRepo = notificationRepo; this.personRepo = personRepo;
    }

    @Transactional
    public Loan createLoan(Long personId, Long bookId, int days) {
        Person p = personRepo.findById(personId).orElseThrow(() -> new IllegalArgumentException("Person not found"));
        Book book = bookRepo.findByIdForUpdate(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getCopies() == null || book.getCopies() <= 0) throw new IllegalStateException("No copies available");

        book.setCopies(book.getCopies() - 1);
        book.setTimesBorrowed((book.getTimesBorrowed()==null?0:book.getTimesBorrowed())+1);
        bookRepo.save(book);

        OffsetDateTime now = OffsetDateTime.now();
        Loan loan = Loan.builder()
                .person(p)
                .book(book)
                .startTime(now)
                .dueDate(now.plusDays(days))
                .status(LoanStatus.ACTIVE)
                .build();
        loan = loanRepo.save(loan);

        // create notification
        String msg = String.format("Empréstimo: '%s' — %s; aluno: %s (matrícula %s); hora: %s",
                book.getTitle(), book.getAuthor(), p.getName(), p.getMatricula(), now.toString());
        Notification n = Notification.builder().loan(loan).type("LOAN_CREATED").message(msg).build();
        notificationRepo.save(n);

        return loan;
    }

    @Transactional
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        if (loan.getStatus() != LoanStatus.ACTIVE) throw new IllegalStateException("Loan not active");

        loan.setReturnedTime(OffsetDateTime.now());
        loan.setStatus(LoanStatus.RETURNED);
        loanRepo.save(loan);

        Book book = bookRepo.findByIdForUpdate(loan.getBook().getId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setCopies(book.getCopies()==null?1:book.getCopies()+1);
        bookRepo.save(book);

        String msg = String.format("Devolução: '%s' — %s; aluno: %s (matrícula %s); hora: %s",
                loan.getBook().getTitle(), loan.getBook().getAuthor(), loan.getPerson().getName(), loan.getPerson().getMatricula(), loan.getReturnedTime().toString());
        notificationRepo.save(Notification.builder().loan(loan).type("LOAN_RETURNED").message(msg).build());

        return loan;
    }
}

