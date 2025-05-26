package com.se.dandan.controller;

import com.se.dandan.dto.IdCheckRequestDTO;
import com.se.dandan.dto.SignInRequestDTO;
import com.se.dandan.dto.SignUpRequestDTO;
import com.se.dandan.dto.TokenInfo;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "아이디 중복 확인 컨트롤러", description = "아이디 중복 확인을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/id-check")
    public ResponseTemplate<?> idCheck(@RequestBody @Valid IdCheckRequestDTO idCheckRequestDTO) {
        authService.idCheck(idCheckRequestDTO);
        return new ResponseTemplate<>(HttpStatus.OK, "사용할 수 있는 아이디 입니다.");
    }

    @Operation(summary = "회원가입 컨트롤러", description = "회원가입을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/sign-up")
    public ResponseTemplate<?> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return new ResponseTemplate<>(HttpStatus.OK, "회원가입 성공");
    }

    @Operation(summary = "로그인 컨트롤러", description = "로그인을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/sign-in")
    public ResponseTemplate<TokenInfo> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO, HttpServletResponse response) {
        TokenInfo token = authService.signIn(signInRequestDTO, response);

        return new ResponseTemplate<>(HttpStatus.OK, "로그인 성공", token);
    }

    @Operation(summary = "로그아웃 컨트롤러", description = "로그아웃을 요청하는 컨트롤러 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/sign-out")
    public ResponseTemplate<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.signOut(token);
        return new ResponseTemplate<>(HttpStatus.OK, "로그아웃 성공");
    }
}
