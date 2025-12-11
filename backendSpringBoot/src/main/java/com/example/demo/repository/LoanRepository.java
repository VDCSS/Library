package com.example.demo.repository;
import com.example.demo.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByUserIdAndReturnedFalse(Long userId);
    Optional<Loan> findByIdAndReturnedFalse(Long id);
    List<Loan> findByExemplarIdAndReturnedFalse(Long exemplarId);
}
