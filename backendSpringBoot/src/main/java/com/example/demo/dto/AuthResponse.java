package com.example.demo.dto;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private Set<String> roles;
    private Long id;
    private String username;
}
