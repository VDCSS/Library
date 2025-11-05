package com.example.demo.service;

import com.example.demo.model.Loan;

public interface NotificationService {
    void notifyLoanCreated(Loan loan);
    void notifyLoanReturned(Loan loan);
    void notifyLoanOverdue(Loan loan);
}
