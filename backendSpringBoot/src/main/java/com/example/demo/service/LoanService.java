package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repository.*;
import com.example.demo.model.*;
import java.time.*;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final ExemplarRepository exemplarRepo;
    private final ReservationRepository reservationRepo;
    private final FineRepository fineRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final ConfigService configService;

    public LoanService(LoanRepository loanRepo, ExemplarRepository exemplarRepo,
                       ReservationRepository reservationRepo, FineRepository fineRepo,
                       UserRepository userRepo, BookRepository bookRepo, ConfigService configService) {
        this.loanRepo = loanRepo; this.exemplarRepo = exemplarRepo; this.reservationRepo = reservationRepo;
        this.fineRepo = fineRepo; this.userRepo = userRepo; this.bookRepo = bookRepo; this.configService = configService;
    }

    @Transactional
    public Loan createLoan(Long userId, Long exemplarId) {
        User user = userRepo.findById(userId).orElseThrow();
        Exemplar ex = exemplarRepo.findById(exemplarId).orElseThrow();
        if(ex.getStatus() != ExemplarStatus.AVAILABLE) throw new RuntimeException("Exemplar not available");
        if(user.getOutstandingFines()!=null && user.getOutstandingFines()>0.0) throw new RuntimeException("Outstanding fines");
        int days = user.getRole()==Role.TEACHER?14:7;
        int renewals = user.getRole()==Role.TEACHER?4:3;
        LocalDateTime now = LocalDateTime.now();
        Loan loan = Loan.builder()
                .user(user).exemplar(ex)
                .loanDate(now)
                .expectedReturnDate(now.plusDays(days))
                .renewalsRemaining(renewals)
                .build();
        ex.setStatus(ExemplarStatus.LOANED);
        exemplarRepo.save(ex);
        return loanRepo.save(loan);
    }

    @Transactional
    public Loan renewLoan(Long loanId, Long userId) {
        Loan loan = loanRepo.findByIdAndReturnedFalse(loanId).orElseThrow(()->new RuntimeException("Loan not active"));
        if(!loan.getUser().getId().equals(userId)) throw new RuntimeException("Not owner");
        if(Boolean.TRUE.equals(loan.getReturned())) throw new RuntimeException("Already returned");
        if(loan.getUser().getOutstandingFines()!=null && loan.getUser().getOutstandingFines()>0.0) throw new RuntimeException("Pay outstanding fines");
        if(loan.getRenewalsRemaining()==null || loan.getRenewalsRemaining()<=0) throw new RuntimeException("No renewals remaining");
        LocalDateTime expected = loan.getExpectedReturnDate();
        long grace = configService.getGraceHours(); // 12
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(expected)) {
            Duration dur = Duration.between(expected, now);
            long hoursLate = dur.toHours();
            if(hoursLate > grace) {
                throw new RuntimeException("Loan too late to renew");
            }
            // else within grace -> allow
        }
        // proceed to renew: increment renewalsDone, decrement renewalsRemaining, push expectedReturnDate by original loan period
        long loanPeriodDays = loan.getUser().getRole()==Role.TEACHER?14:7;
        loan.setExpectedReturnDate(loan.getExpectedReturnDate().plusDays(loanPeriodDays));
        loan.setRenewalsDone(loan.getRenewalsDone()+1);
        loan.setRenewalsRemaining(loan.getRenewalsRemaining()-1);
        return loanRepo.save(loan);
    }

    @Transactional
    public Loan returnLoan(Long loanId, Long userId) {
        Loan loan = loanRepo.findByIdAndReturnedFalse(loanId).orElseThrow(()->new RuntimeException("Loan not active"));
        if(!loan.getUser().getId().equals(userId)) throw new RuntimeException("Not owner");
        LocalDateTime now = LocalDateTime.now();
        loan.setRealReturnDate(now);
        loan.setReturned(true);
        LocalDateTime expected = loan.getExpectedReturnDate();
        long grace = configService.getGraceHours();
        if(now.isAfter(expected)) {
            Duration dur = Duration.between(expected, now);
            long hoursLate = dur.toHours();
            long chargedHours = Math.max(0, hoursLate - grace);
            if(chargedHours>0) {
                long daysCharged = (chargedHours + 23) / 24; // ceil
                BigDecimal rate = configService.getRatePerDay();
                BigDecimal amount = rate.multiply(BigDecimal.valueOf(daysCharged));
                Fine fine = Fine.builder()
                        .user(loan.getUser())
                        .loan(loan)
                        .createdAt(LocalDateTime.now())
                        .daysOverdue((int)daysCharged)
                        .ratePerDay(rate)
                        .amount(amount)
                        .type("LATE")
                        .status("PENDING")
                        .build();
                fineRepo.save(fine);
                loan.setDaysOverdueAtReturn((int)daysCharged);
                loan.setFineGeneratedId(fine.getId());
                Double outstanding = loan.getUser().getOutstandingFines();
                loan.getUser().setOutstandingFines((outstanding==null?0.0:outstanding)+amount.doubleValue());
                userRepo.save(loan.getUser());
            }
        }
        Exemplar ex = loan.getExemplar();
        List<Reservation> queue = reservationRepo.findByBookIdOrderByReservedAtAsc(ex.getBook().getId());
        if(queue!=null && !queue.isEmpty()) {
            ex.setStatus(ExemplarStatus.RESERVED);
            Reservation first = queue.get(0);
            first.setNotified(true);
            first.setExpiresAt(LocalDateTime.now().plusHours(48));
            reservationRepo.save(first);
        } else {
            ex.setStatus(ExemplarStatus.AVAILABLE);
        }
        exemplarRepo.save(ex);
        return loanRepo.save(loan);
    }
}
