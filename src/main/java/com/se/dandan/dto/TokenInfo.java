package com.se.dandan.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenInfo {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    private String role;
}
