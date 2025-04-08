package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequestDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
