package com.se.dandan.controller;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.entity.Word;
import com.se.dandan.service.ExampleService;
import com.se.dandan.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/words")
public class WordController {

    private final WordService wordService;
    private final ExampleService exampleService;

    public WordController(WordService wordService,  ExampleService exampleService) {
        this.wordService = wordService;
        this.exampleService = exampleService;
    }

    @Operation(summary = "단어 학습 컨트롤러", description = "학습할 단어들을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/today")
    public ResponseTemplate<List<Word>> getTodayWords(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        Long memberId = memberPrincipalDTO.getId();

        List<Word> words = wordService.getTodayWords(memberId);

        return new ResponseTemplate<>(HttpStatus.OK, "단어 조회 성공", words);
    }

    @Operation(summary = "AI 예문 생성 컨트롤러", description = "AI 예문 생성을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/{wordId}/example")
    public ResponseTemplate<String> generateExample(@PathVariable Long wordId) {
        Word word = wordService.findById(wordId);
        String example = exampleService.generateExample(word.getEnglish(), List.of(word.getMeaning1(), word.getMeaning2(), word.getMeaning3()));
        return new ResponseTemplate<>(HttpStatus.OK, "예문 조회 성공", example);
    }

    @Operation(summary = "단어 진도율 확인 컨트롤러", description = "단어 진도율 확인을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/{wordId}/memorized")
    public ResponseTemplate<Void> markAsMemorized(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO, @PathVariable Long wordId) {
        Long memberId = memberPrincipalDTO.getId();
        wordService.markedMemorized(memberId, wordId);
        return new ResponseTemplate<>(HttpStatus.OK, "학습 완료");
    }
}
