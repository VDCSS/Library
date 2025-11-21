package com.example.demo.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Loan loan;

    private OffsetDateTime createdAt;

    private String type; // LOAN_CREATED, LOAN_RETURNED
    @Column(columnDefinition = "TEXT")
    private String message;
    private boolean isRead = false;

    @PrePersist
    public void pre() { if (createdAt == null) createdAt = OffsetDateTime.now(); }
}
