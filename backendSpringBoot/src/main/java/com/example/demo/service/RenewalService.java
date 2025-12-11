package com.example.demo.service;

import com.example.demo.model.Loan;

public interface RenewalService {
    /**
     * Aplica regras de renovação ao empréstimo.
     * Deve lançar RuntimeException (BadRequest/Forbidden) em caso de negação.
     */
    Loan renew(Loan loan);
}
