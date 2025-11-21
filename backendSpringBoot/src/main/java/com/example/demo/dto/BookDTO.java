package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer timesBorrowed;
}
