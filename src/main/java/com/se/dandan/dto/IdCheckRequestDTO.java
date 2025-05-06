package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class IdCheckRequestDTO {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{7,15}$",
            message = "아이디는 영문자와 숫자를 포함하여 7자 이상 15자 이하로 입력해주세요.") // 영문자, 숫자를 포함한 7글자 이상 15글자 이하
    private String userId;

}
