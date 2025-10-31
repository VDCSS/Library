package com.example.demo.dto;

import lombok.Data;

@Data
public class LoanRequest {
    private Long bookId;
    private Long studentId;
}
