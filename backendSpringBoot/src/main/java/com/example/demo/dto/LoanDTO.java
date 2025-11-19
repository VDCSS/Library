package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {
    private Long id;

    private Long bookId;
    private String bookTitle;
    private String bookAuthor;

    private Long personId;
    private String personName;
    private String personEmail;
    private String personPhone;

    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    private String status;
}
