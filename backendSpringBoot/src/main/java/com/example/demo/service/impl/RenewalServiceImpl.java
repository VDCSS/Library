package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.Loan;
import com.example.demo.service.RenewalService;
import org.springframework.stereotype.Service;

/**
 * Serviço focado apenas nas regras de decremento e verificação de renovações.
 * LoanServiceImpl delega a ele as regras de contagem.
 */
@Service
public class RenewalServiceImpl implements RenewalService {

    @Override
    public Loan renew(Loan loan) {
        if (loan.getRenewalsRemaining() == null || loan.getRenewalsRemaining() <= 0) {
            throw new BadRequestException("No renewals remaining");
        }
        loan.setRenewalsDone((loan.getRenewalsDone() == null ? 0 : loan.getRenewalsDone()) + 1);
        loan.setRenewalsRemaining(loan.getRenewalsRemaining() - 1);
        return loan;
    }
}
