package com.example.demo.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String message;
}
