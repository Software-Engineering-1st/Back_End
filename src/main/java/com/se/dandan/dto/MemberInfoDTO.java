package com.se.dandan.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDTO {

    private String nickname;

    private String userId;

    private String password;
}
