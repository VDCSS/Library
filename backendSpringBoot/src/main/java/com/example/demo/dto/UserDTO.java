package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String cpf;

    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String phone;
}
