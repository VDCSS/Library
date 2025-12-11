package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.LoanService;
import com.example.demo.dto.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.mapper.DtoMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;
    private final UserRepository userRepo;
    public LoanController(LoanService loanService, UserRepository userRepo){ this.loanService = loanService; this.userRepo = userRepo; }

    @PostMapping("/create")
    public LoanDto create(@RequestBody CreateLoanRequest req, @AuthenticationPrincipal UserDetails ud){
        Long userId = userRepo.findByEmail(ud.getUsername()).orElseThrow(() -> new com.example.demo.exception.NotFoundException("User not found")).getId();
        return DtoMapper.toLoanDto(loanService.createLoan(userId, req.getExemplarId()));
    }

    @PutMapping("/{id}/renew")
    public LoanDto renew(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud){
        Long userId = userRepo.findByEmail(ud.getUsername()).orElseThrow(() -> new com.example.demo.exception.NotFoundException("User not found")).getId();
        return DtoMapper.toLoanDto(loanService.renewLoan(id, userId));
    }

    @PutMapping("/{id}/return")
    public LoanDto returnLoan(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud){
        Long userId = userRepo.findByEmail(ud.getUsername()).orElseThrow(() -> new com.example.demo.exception.NotFoundException("User not found")).getId();
        return DtoMapper.toLoanDto(loanService.returnLoan(id, userId));
    }
}
