package com.se.dandan.controller;

import com.se.dandan.dto.MemberInfoDTO;
import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.WordCountDTO;
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

    @GetMapping("/my-page/get-nickname")
    public ResponseTemplate<String> getNickname(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        String nickname = memberPrincipalDTO.getNickname();

        return new ResponseTemplate<>(HttpStatus.OK, "회원 닉네임 조회 성공", nickname);
    }

    @GetMapping("/my-page/get-userId")
    public ResponseTemplate<String> getUserId(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        String userId = memberPrincipalDTO.getUserId();

        return new ResponseTemplate<>(HttpStatus.OK, "회원 아이디 조회 성공", userId);
    }

    @GetMapping("/my-page/get-word-count")
    public ResponseTemplate<Integer> getWordCount(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        String userId = memberPrincipalDTO.getUserId();
        int wordCount = memberService.getWordCount(userId);
        return new ResponseTemplate<>(HttpStatus.OK, "회원 목표 단어 개수 조회 성공", wordCount);
    }

    @PostMapping("/my-page/edit-info")
    public ResponseTemplate<?> editMemberInfo(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO,
                                              @RequestBody MemberInfoDTO memberInfoDTO) {
        String userId = memberPrincipalDTO.getUserId();
        memberService.updateMemberInfo(userId, memberInfoDTO);

        return new ResponseTemplate<>(HttpStatus.OK, "회원정보 수정 성공");
    }

    @PostMapping("/my-page/edit-word-count")
    public ResponseTemplate<Integer> editWordCount(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO,
                                                   @RequestBody WordCountDTO wordCountDTO) {
        String userId = memberPrincipalDTO.getUserId();
        memberService.updateWordCount(userId, wordCountDTO);

        return new ResponseTemplate<>(HttpStatus.OK, "목표 단어 개수 수정 성공");
    }
}
