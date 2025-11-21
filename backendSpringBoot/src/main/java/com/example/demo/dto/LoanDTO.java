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

    // Dados do Livro
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;

    // Dados da Pessoa
    private Long personId;
    private String personName;
    private String personEmail;
    private String personPhone;

    // Datas
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    // Status (Ex: "ACTIVE", "RETURNED")
    private String status;
}
