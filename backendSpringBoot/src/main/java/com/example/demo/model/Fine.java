package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name="fines")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Fine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Loan loan;

    private LocalDateTime createdAt;
    private Integer daysOverdue;
    private BigDecimal ratePerDay;
    private BigDecimal amount;
    private String type; // LATE, DAMAGE, LOSS, ADJUSTMENT
    private String status; // PENDING, PAID, WAIVED
    private String description;
}
