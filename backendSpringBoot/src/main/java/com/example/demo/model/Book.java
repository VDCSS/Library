package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private String category;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Exemplar> exemplars;
}
