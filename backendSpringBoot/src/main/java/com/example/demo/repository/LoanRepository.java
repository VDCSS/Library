package com.example.demo.repository;

import com.example.demo.model.Loan;
import com.example.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByPersonId(Long personId);
    List<Loan> findByStatus(String status);
}
