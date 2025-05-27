package com.se.dandan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "단어 문제 DTO")
@Data
@Builder
public class TestQuestionDTO {

    @Schema(description = "단어 PK값")
    private Long wordId;

    private String meaning1;

    private String meaning2;

    private String meaning3;
}
