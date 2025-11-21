package com.example.demo.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;
    private String author;
    private String isbn;

    private Integer copies = 1;
    private Integer timesBorrowed = 0;

    @Version
    private Long version;
}
