package com.se.dandan.controller;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.WrongNoteDTO;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.repository.WrongNoteRepository;
import com.se.dandan.service.WrongNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Wrong Note API", description = "오답노트 API")
@RestController
@RequestMapping("api/v1/wrong-notes")
@RequiredArgsConstructor
public class WrongNoteController {

    private final WrongNoteService wrongNoteService;

    @Operation(summary = "오답노트 조회 컨트롤러", description = "오답노트 조회를 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/dates")
    public ResponseTemplate<List<LocalDate>> getWrongNoteDates(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO) {
        Long memberId = memberPrincipalDTO.getId();

        List<LocalDate> wrongNoteDates = wrongNoteService.getWrongNoteDates(memberId);

        return new ResponseTemplate<>(HttpStatus.OK, "조회 성공", wrongNoteDates);
    }

    @Operation(summary = "오답 노트 상세 조회 컨트롤러", description = "오답 노트 상세 조회를 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping
    public ResponseTemplate<List<WrongNoteDTO>> getWrongNotesByDate(@AuthenticationPrincipal MemberPrincipalDTO memberPrincipalDTO, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long memberId = memberPrincipalDTO.getId();
        List<WrongNoteDTO> wrongNoteDTOs = wrongNoteService.getWrongNotesByDate(memberId, date);
        return new ResponseTemplate<>(HttpStatus.OK, "조회 성공", wrongNoteDTOs);
    }
}
