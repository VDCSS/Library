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
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) private Person person;
    @ManyToOne(optional=false) private Book book;

    private OffsetDateTime startTime;
    private OffsetDateTime dueDate;
    private OffsetDateTime returnedTime;

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;
}
