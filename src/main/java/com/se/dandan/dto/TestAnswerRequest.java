package com.se.dandan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "답안 제출 DTO")
@Data
public class TestAnswerRequest {

    @Schema(description = "단어 PK값")
    private Long wordId;

    @Schema(description = "사용자가 제출한 정답")
    private String userAnswer;
}
