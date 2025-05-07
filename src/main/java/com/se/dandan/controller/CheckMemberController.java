package com.se.dandan.controller;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CheckMember API", description = "현재 사용자 정보 추출 API")
@RestController
public class CheckMemberController {

    @Operation(summary = "사용자 확인 컨트롤러", description = "사용자 확인을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/test")
    public ResponseTemplate<String> test(@AuthenticationPrincipal MemberPrincipalDTO member) {

        String nickname = member.getNickname();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        return new ResponseTemplate<>(HttpStatus.OK, "환영합니다, " + role + ", " + nickname + "님!", nickname);
    }
}
