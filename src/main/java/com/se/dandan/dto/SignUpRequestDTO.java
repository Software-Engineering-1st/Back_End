package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @NotBlank
    @Size(min = 4, max = 6, message = "닉네임은 4자 이상 6자 이하로 입력해주세요.")
    @Pattern(
            regexp = "^(?!\\d+$)[a-zA-Z0-9가-힣]+$",
            message = "닉네임은 한글, 영문자, 숫자만 사용 가능하며 숫자만으로 구성할 수 없습니다."
    )
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{7,15}$",
            message = "아이디는 영문자와 숫자를 포함하여 7자 이상 15자 이하로 입력해주세요.")
    private String userId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 최소 8자 이상이어야 합니다.")
    private String password;

    private String checkPassword;

    @NotNull
    private int wordCount;

}
