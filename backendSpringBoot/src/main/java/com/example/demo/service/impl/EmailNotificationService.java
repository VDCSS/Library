package com.example.demo.service.impl;

import com.example.demo.model.Loan;
import com.example.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    @Override
    public void notifyLoanCreated(Loan loan) {
        logger.info("notifyLoanCreated -> loanId={}, person={}, book={}", loan.getId(), loan.getUser().getEmail(), loan.getBook().getTitle());
    }

    @Override
    public void notifyLoanReturned(Loan loan) {
        logger.info("notifyLoanReturned -> loanId={}, person={}, book={}", loan.getId(), loan.getUser().getEmail(), loan.getBook().getTitle());
    }

    @Override
    public void notifyLoanOverdue(Loan loan) {
        logger.info("notifyLoanOverdue -> loanId={}, person={}, book={}", loan.getId(), loan.getUser().getEmail(), loan.getBook().getTitle());
    }
}
