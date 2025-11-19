package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String author;

    private String isbn;

    // number of copies available
    private Integer copies = 1;

    // derived: if copies > 0 then available
    public boolean isAvailable() {
        return copies != null && copies > 0;
    }

    // optimistic locking version
    @Version
    private Long version;

    private Integer timesBorrowed = 0;
}
