package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final NotificationRepository notificationRepo;
    private final PersonRepository personRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo,
                       NotificationRepository notificationRepo, PersonRepository personRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.notificationRepo = notificationRepo;
        this.personRepo = personRepo;
    }

    @Transactional
    public Loan createLoan(Long personId, Long bookId, int days) {
        Person p = personRepo.findById(personId).orElseThrow(() -> new IllegalArgumentException("Person not found"));
        Book book = bookRepo.findByIdForUpdate(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() <= 0) throw new IllegalStateException("No copies available");

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        book.setTimesBorrowed((book.getTimesBorrowed()==null?0:book.getTimesBorrowed())+1);
        bookRepo.save(book);

        LocalDateTime now = LocalDateTime.now();
        Loan loan = Loan.builder()
                .person(p)
                .book(book)
                .loanTime(now)
                .dueTime(now.plusDays(days))
                .status("ACTIVE")
                .build();
        loan = loanRepo.save(loan);

        String msg = String.format("Empréstimo: '%s' — %s; aluno: %s (matrícula %s); hora: %s",
                book.getTitle(), book.getAuthor(), p.getName(), p.getMatricula(), now.toString());
        Notification n = Notification.builder().loan(loan).type("LOAN_CREATED").message(msg).build();
        notificationRepo.save(n);

        return loan;
    }

    @Transactional
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        if (!"ACTIVE".equals(loan.getStatus())) throw new IllegalStateException("Loan not active");

        loan.setReturnTime(LocalDateTime.now());
        loan.setStatus("RETURNED");
        loanRepo.save(loan);

        Book book = bookRepo.findByIdForUpdate(loan.getBook().getId()).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        book.setAvailableQuantity(book.getAvailableQuantity()==null?1:book.getAvailableQuantity()+1);
        bookRepo.save(book);

        String msg = String.format("Devolução: '%s' — %s; aluno: %s (matrícula %s); hora: %s",
                loan.getBook().getTitle(), loan.getBook().getAuthor(), loan.getPerson().getName(), loan.getPerson().getMatricula(), loan.getReturnTime().toString());
        notificationRepo.save(Notification.builder().loan(loan).type("LOAN_RETURNED").message(msg).build());

        return loan;
    }

    public List<Loan> findAll() { return loanRepo.findAll(); }
    public Loan findById(Long id) { return loanRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Loan not found")); }
    public List<Loan> findByPerson(Long personId) { return loanRepo.findByPersonId(personId); }
}
