package com.example.demo.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genre;

    @PositiveOrZero
    private Integer quantity = 1;

    private LocalDate addedDate = LocalDate.now();

    private Boolean available = true;

    private Integer timesBorrowed = 0;
}
