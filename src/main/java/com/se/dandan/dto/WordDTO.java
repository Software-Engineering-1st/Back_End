package com.se.dandan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WordDTO {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$")
    private String english;

    @NotBlank
    private String meaning1;

    private String meaning2;

    private String meaning3;
}
