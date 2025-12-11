package com.example.demo.dto;
import lombok.*; import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanDto {
    private Long id;
    private Long userId;
    private Long exemplarId;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime realReturnDate;
    private Integer renewalsRemaining;
    private Integer renewalsDone;
    private Boolean returned;
}
