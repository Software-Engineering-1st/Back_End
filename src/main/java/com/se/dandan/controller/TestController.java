package com.se.dandan.controller;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.TestAnswerRequest;
import com.se.dandan.dto.TestQuestionDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Word Test API", description = "단어 테스트 API")
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;


    @Operation(summary = "문제 생성 컨트롤러", description = "문제 생성을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/questions")
    public ResponseTemplate<List<TestQuestionDTO>> getTestQuestions(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        Long memberId = memberPrincipalDTO.getId();
        List<TestQuestionDTO> questions = testService.generateTestQuestions(memberId);

        return new ResponseTemplate<>(HttpStatus.OK, "문제 조회 성공",  questions);
    }

    @Operation(summary = "답안 제출 컨트롤러", description = "답안 제출 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/answer")
    public ResponseTemplate<Map<String, Boolean>> checkAnswer(@RequestBody TestAnswerRequest testAnswerRequest, @AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        Long memberId = memberPrincipalDTO.getId();

        boolean isCorrect = testService.checkAnswerAndSaveWrongNote(memberId, testAnswerRequest.getWordId(), testAnswerRequest.getUserAnswer());

        return new ResponseTemplate<>(HttpStatus.OK, "답안 제출 성공", Map.of("isCorrect", isCorrect));
    }
}
