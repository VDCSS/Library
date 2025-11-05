package com.example.demo.scheduling;

import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.events.LoanReturnedEvent;
import com.example.demo.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OverdueChecker {

    private static final Logger logger = LoggerFactory.getLogger(OverdueChecker.class);

    private final LoanRepository loanRepo;
    private final ApplicationEventPublisher publisher;

    public OverdueChecker(LoanRepository loanRepo, ApplicationEventPublisher publisher) {
        this.loanRepo = loanRepo;
        this.publisher = publisher;
    }

    // runs every 10 minutes by default (adjust in application.properties)
    @Scheduled(fixedRateString = "${scheduler.overdue-check-ms:600000}")
    @Transactional
    public void checkOverdueLoans() {
        LocalDateTime now = LocalDateTime.now();
        List<Loan> overdue = loanRepo.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE && l.getReturnDate() != null && l.getReturnDate().isBefore(now))
                .toList();

        if (overdue.isEmpty()) {
            logger.debug("No overdue loans at {}", now);
            return;
        }

        for (Loan l : overdue) {
            l.setStatus(LoanStatus.OVERDUE);
            loanRepo.save(l);
            logger.info("Marked loan {} as OVERDUE", l.getId());
            publisher.publishEvent(new LoanReturnedEvent(this, l)); // re-use returned event for notification
        }
    }
}
