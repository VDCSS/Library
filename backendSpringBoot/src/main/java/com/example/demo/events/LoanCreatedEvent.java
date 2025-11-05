package com.example.demo.events;

import com.example.demo.model.Loan;
import org.springframework.context.ApplicationEvent;

public class LoanCreatedEvent extends ApplicationEvent {
    private final Loan loan;

    public LoanCreatedEvent(Object source, Loan loan) {
        super(source);
        this.loan = loan;
    }

    public Loan getLoan() { return loan; }
}
