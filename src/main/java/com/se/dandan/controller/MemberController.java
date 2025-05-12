package com.se.dandan.controller;

import com.se.dandan.dto.MemberInfoDTO;
import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/my-page")
    public ResponseTemplate<MemberInfoDTO> memberInfo(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        String userId = memberPrincipalDTO.getUserId();
        MemberInfoDTO memberInfo = memberService.loadMemberInfo(userId);

        return new ResponseTemplate<>(HttpStatus.OK, "회원 정보 조회 성공", memberInfo);
    }

    @PostMapping("/my-page/edit")
    public ResponseTemplate<?> editMemberInfo(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO,
                                              @RequestBody MemberInfoDTO memberInfoDTO) {
        String userId = memberPrincipalDTO.getUserId();
        memberService.updateMemberInfo(userId, memberInfoDTO);

        return new ResponseTemplate<>(HttpStatus.OK, "회원정보 수정 성공");
    }
}
