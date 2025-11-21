package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "loan_time")
    private LocalDateTime loanTime;

    @Column(name = "due_time")
    private LocalDateTime dueTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "status")
    private String status; // "ACTIVE" or "RETURNED"
}
