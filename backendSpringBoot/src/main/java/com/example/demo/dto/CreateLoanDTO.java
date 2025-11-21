package com.example.demo.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanDTO {
    // If admin registers, personId must be provided; if student, frontend can omit and backend resolves from token
    private Long personId;
    @NotNull
    private Long bookId;
    private Integer days = 7;
}
