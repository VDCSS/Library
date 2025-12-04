package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="exemplars")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exemplar {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @Column(unique=true)
    private String barcode;

    @Enumerated(EnumType.STRING)
    private ExemplarStatus status = ExemplarStatus.AVAILABLE;
}
