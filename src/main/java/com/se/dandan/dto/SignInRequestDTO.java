package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;
}
