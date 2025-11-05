package com.example.demo.listener;

import com.example.demo.events.LoanCreatedEvent;
import com.example.demo.events.LoanReturnedEvent;
import com.example.demo.service.NotificationService;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoanEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoanEventListener.class);

    private final BookRepository bookRepo;
    private final NotificationService notificationService;

    public LoanEventListener(BookRepository bookRepo, NotificationService notificationService) {
        this.bookRepo = bookRepo;
        this.notificationService = notificationService;
    }

    @EventListener
    @Transactional
    public void handleLoanCreated(LoanCreatedEvent event) {
        Loan loan = event.getLoan();
        Book book = loan.getBook();
        if (book != null) {
            book.setTimesBorrowed((book.getTimesBorrowed() == null ? 1 : book.getTimesBorrowed() + 1));
            bookRepo.save(book);
            logger.info("Book {} timesBorrowed updated to {}", book.getId(), book.getTimesBorrowed());
        }
        notificationService.notifyLoanCreated(loan);
    }

    @EventListener
    public void handleLoanReturned(LoanReturnedEvent event) {
        Loan loan = event.getLoan();
        notificationService.notifyLoanReturned(loan);
        logger.info("Loan {} returned", loan.getId());
    }
}
