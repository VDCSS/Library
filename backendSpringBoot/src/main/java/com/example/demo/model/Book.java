package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    private String isbn;

    @Column(name = "total_quantity")
    private Integer totalQuantity = 1;

    @Column(name = "available_quantity")
    private Integer availableQuantity = 1;

    @Column(name = "times_borrowed")
    private Integer timesBorrowed = 0;

    @Version
    private Long version;
}
