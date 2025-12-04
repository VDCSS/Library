package com.example.demo.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private String category;
    private Integer totalExemplars;
    private Integer availableExemplars;
}
