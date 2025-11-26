package com.example.demo.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanDTO {
    private Long personId;
    @NotNull
    private Long bookId;
    private Integer days = 7;
}
