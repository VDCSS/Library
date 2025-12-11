package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="loans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Exemplar exemplar;

    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime realReturnDate;

    private Integer renewalsRemaining;
    private Integer renewalsDone = 0;

    private Boolean returned = false;
    private Integer daysOverdueAtReturn;
    private Long fineGeneratedId;
}
