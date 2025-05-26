package com.se.dandan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "목표 단어 개수 DTO")
@Data
public class WordCountDTO {

    @Schema(description = "목표 단어 개수")
    private int wordCount;
}
