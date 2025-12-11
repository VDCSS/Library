package com.example.demo.service;

import com.example.demo.model.Loan;
import java.util.List;

public interface LoanService {
    Loan createLoan(Long userId, Long exemplarId);
    Loan renewLoan(Long loanId, Long userId);
    Loan returnLoan(Long loanId, Long userId);
    List<Loan> findActiveLoansByUser(Long userId);
    Loan findById(Long id);
}
