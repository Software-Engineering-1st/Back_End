package com.se.dandan.controller;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckMemberController {

    @GetMapping("/test")
    public ResponseTemplate<String> test(@AuthenticationPrincipal MemberPrincipalDTO member) {

        String nickname = member.getNickname();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        return new ResponseTemplate<>(HttpStatus.OK, "환영합니다, " + role + ", " + nickname + "님!", nickname);
    }
}
