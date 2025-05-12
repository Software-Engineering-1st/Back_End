package com.se.dandan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "로그인 시, 사용자 정보 저장 DTO")
@Data
@Builder
public class MemberPrincipalDTO {

    @Schema(description = "DB 상의 PK 값")
    private Long id;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 아이디")
    private String userId;
}
