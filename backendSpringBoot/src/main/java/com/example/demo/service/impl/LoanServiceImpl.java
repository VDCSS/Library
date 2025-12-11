package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ConfigService;
import com.example.demo.service.LoanService;
import com.example.demo.service.RenewalService;
import com.example.demo.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepo;
    private final ExemplarRepository exemplarRepo;
    private final ReservationRepository reservationRepo;
    private final FineRepository fineRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final ConfigService configService;
    private final RenewalService renewalService;
    private final NotificationService notificationService;

    public LoanServiceImpl(LoanRepository loanRepo,
                           ExemplarRepository exemplarRepo,
                           ReservationRepository reservationRepo,
                           FineRepository fineRepo,
                           UserRepository userRepo,
                           BookRepository bookRepo,
                           ConfigService configService,
                           RenewalService renewalService,
                           NotificationService notificationService) {
        this.loanRepo = loanRepo;
        this.exemplarRepo = exemplarRepo;
        this.reservationRepo = reservationRepo;
        this.fineRepo = fineRepo;
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.configService = configService;
        this.renewalService = renewalService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Loan createLoan(Long userId, Long exemplarId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Exemplar ex = exemplarRepo.findById(exemplarId).orElseThrow(() -> new NotFoundException("Exemplar not found"));
        if (ex.getStatus() != ExemplarStatus.AVAILABLE) throw new BadRequestException("Exemplar not available");
        if (Boolean.TRUE.equals(user.getBlocked())) throw new ForbiddenException("User blocked");
        if (user.getOutstandingFines() != null && user.getOutstandingFines() > 0.0) throw new ForbiddenException("Outstanding fines exist");
        int days = user.getRole() == Role.TEACHER ? 14 : 7;
        int renewals = user.getRole() == Role.TEACHER ? 4 : 3;
        LocalDateTime now = LocalDateTime.now();
        Loan loan = Loan.builder()
                .user(user)
                .exemplar(ex)
                .loanDate(now)
                .expectedReturnDate(now.plusDays(days))
                .renewalsRemaining(renewals)
                .renewalsDone(0)
                .returned(false)
                .build();
        ex.setStatus(ExemplarStatus.LOANED);
        exemplarRepo.save(ex);
        Loan saved = loanRepo.save(loan);
        notificationService.notifyUser(user, "Loan created", "Empréstimo criado: exemplarId=" + ex.getId());
        return saved;
    }

    @Override
    @Transactional
    public Loan renewLoan(Long loanId, Long userId) {
        Loan loan = loanRepo.findByIdAndReturnedFalse(loanId).orElseThrow(() -> new NotFoundException("Loan not active"));
        if (!loan.getUser().getId().equals(userId)) throw new ForbiddenException("Not owner");
        if (Boolean.TRUE.equals(loan.getReturned())) throw new BadRequestException("Already returned");
        if (loan.getUser().getOutstandingFines() != null && loan.getUser().getOutstandingFines() > 0.0)
            throw new ForbiddenException("Outstanding fines");
        if (loan.getRenewalsRemaining() == null || loan.getRenewalsRemaining() <= 0) throw new BadRequestException("No renewals remaining");

        // check grace hours
        LocalDateTime expected = loan.getExpectedReturnDate();
        long grace = configService.getGraceHours();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expected)) {
            Duration dur = Duration.between(expected, now);
            long hoursLate = dur.toHours();
            if (hoursLate > grace) throw new BadRequestException("Loan too late to renew");
            // within grace allowed
        }

        // reservation does not block renewals if renewalsRemaining>0 (rule)
        // apply renewal counting via RenewalService
        loan = renewalService.renew(loan);

        long loanPeriodDays = loan.getUser().getRole() == Role.TEACHER ? 14 : 7;
        loan.setExpectedReturnDate(loan.getExpectedReturnDate().plusDays(loanPeriodDays));
        Loan saved = loanRepo.save(loan);
        notificationService.notifyUser(saved.getUser(), "Loan renewed", "Empréstimo renovado: id=" + saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Loan returnLoan(Long loanId, Long userId) {
        Loan loan = loanRepo.findByIdAndReturnedFalse(loanId).orElseThrow(() -> new NotFoundException("Loan not active"));
        if (!loan.getUser().getId().equals(userId)) throw new ForbiddenException("Not owner");
        LocalDateTime now = LocalDateTime.now();
        loan.setRealReturnDate(now);
        loan.setReturned(true);

        LocalDateTime expected = loan.getExpectedReturnDate();
        long grace = configService.getGraceHours();
        if (now.isAfter(expected)) {
            Duration dur = Duration.between(expected, now);
            long hoursLate = dur.toHours();
            long chargedHours = Math.max(0, hoursLate - grace);
            if (chargedHours > 0) {
                long daysCharged = (chargedHours + 23) / 24;
                BigDecimal rate = configService.getRatePerDay();
                BigDecimal amount = rate.multiply(BigDecimal.valueOf(daysCharged));
                Fine fine = Fine.builder()
                        .user(loan.getUser())
                        .loan(loan)
                        .createdAt(LocalDateTime.now())
                        .daysOverdue((int) daysCharged)
                        .ratePerDay(rate)
                        .amount(amount)
                        .type("LATE")
                        .status("PENDING")
                        .build();
                fineRepo.save(fine);
                loan.setDaysOverdueAtReturn((int) daysCharged);
                loan.setFineGeneratedId(fine.getId());
                Double outstanding = loan.getUser().getOutstandingFines();
                loan.getUser().setOutstandingFines((outstanding == null ? 0.0 : outstanding) + amount.doubleValue());
                userRepo.save(loan.getUser());
                notificationService.notifyUser(loan.getUser(), "Fine generated", "Multa gerada: R$ " + amount);
            }
        }

        Exemplar ex = loan.getExemplar();
        List<Reservation> queue = reservationRepo.findByBookIdOrderByReservedAtAsc(ex.getBook().getId());
        if (queue != null && !queue.isEmpty()) {
            ex.setStatus(ExemplarStatus.RESERVED);
            Reservation first = queue.get(0);
            first.setNotified(true);
            first.setExpiresAt(LocalDateTime.now().plusHours(48));
            reservationRepo.save(first);
            notificationService.notifyUser(first.getUser(), "Book available", "O livro solicitado está disponível: bookId=" + ex.getBook().getId());
        } else {
            ex.setStatus(ExemplarStatus.AVAILABLE);
        }
        exemplarRepo.save(ex);
        Loan saved = loanRepo.save(loan);
        notificationService.notifyUser(saved.getUser(), "Loan returned", "Empréstimo devolvido: id=" + saved.getId());
        return saved;
    }

    @Override
    public List<Loan> findActiveLoansByUser(Long userId) {
        return loanRepo.findByUserIdAndReturnedFalse(userId);
    }

    @Override
    public Loan findById(Long id) {
        return loanRepo.findById(id).orElseThrow(() -> new NotFoundException("Loan not found"));
    }
}
