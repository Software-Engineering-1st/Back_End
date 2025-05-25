package com.se.dandan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "사용자 마이페이지 정보 DTO")
@Data
@Builder
public class MemberInfoDTO {

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "사용자 비밀번호")
    private String password;
}
