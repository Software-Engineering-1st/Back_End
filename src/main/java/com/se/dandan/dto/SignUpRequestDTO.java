package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{7,15}$") // 영문자, 숫자를 포함한 7글자 이상 15글자 이하
    private String userId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$") // 영문자, 특수문자, 숫자를 포함한 최소 8자 이상의 문자
    private String password;

    private String checkPassword;

    @NotBlank
    private int wordCount;

}
