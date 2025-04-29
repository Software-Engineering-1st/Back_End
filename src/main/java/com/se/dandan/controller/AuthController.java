package com.se.dandan.controller;

import com.se.dandan.dto.IdCheckRequestDTO;
import com.se.dandan.dto.SignInRequestDTO;
import com.se.dandan.dto.SignUpRequestDTO;
import com.se.dandan.dto.TokenInfo;
import com.se.dandan.dto.common.ResponseTemplate;
import com.se.dandan.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/id-check")
    public ResponseTemplate<?> idCheck(@RequestBody @Valid IdCheckRequestDTO idCheckRequestDTO) {
        authService.idCheck(idCheckRequestDTO);
        return new ResponseTemplate<>(HttpStatus.OK, "사용할 수 있는 아이디 입니다.");
    }

    @PostMapping("/sign-up")
    public ResponseTemplate<?> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return new ResponseTemplate<>(HttpStatus.OK, "회원가입 성공");
    }

    @PostMapping("/sign-in")
    public ResponseTemplate<TokenInfo> signIn(@RequestBody @Valid SignInRequestDTO signInRequestDTO, HttpServletResponse response) {
        TokenInfo token = authService.signIn(signInRequestDTO, response);

        return new ResponseTemplate<>(HttpStatus.OK, "로그인 성공", token);
    }
}
