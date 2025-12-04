package com.example.demo.repository;

import com.example.demo.model.Exemplar;
import com.example.demo.model.ExemplarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExemplarRepository extends JpaRepository<Exemplar,Long> {
    List<Exemplar> findByBookIdAndStatus(Long bookId, ExemplarStatus status);
    long countByBookIdAndStatus(Long bookId, ExemplarStatus status);
}
