package com.se.dandan;

import com.se.dandan.dto.SignUpRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 잘못된_닉네임은_검증_실패한다() {
        SignUpRequestDTO dto = new SignUpRequestDTO();
        dto.setNickname("1234");
        dto.setUserId("test123");
        dto.setPassword("password1!");
        dto.setCheckPassword("password1!");
        dto.setWordCount(10);

        Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nickname")));
    }

    @Test
    void 잘못된_아이디는_검증_실패한다() {
        SignUpRequestDTO dto = new SignUpRequestDTO();
        dto.setNickname("홍길동");
        dto.setUserId("abcdefg");
        dto.setPassword("password1!");
        dto.setCheckPassword("password1!");
        dto.setWordCount(10);

        Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    void 잘못된_비밀번호는_검증_실패한다() {
        SignUpRequestDTO dto = new SignUpRequestDTO();
        dto.setNickname("홍길동");
        dto.setUserId("test123");
        dto.setPassword("password");
        dto.setCheckPassword("password");
        dto.setWordCount(10);

        Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void 목표단어_미입력시_검증_실패한다() {
        SignUpRequestDTO dto = new SignUpRequestDTO();
        dto.setNickname("홍길동");
        dto.setUserId("test123");
        dto.setPassword("password1!");
        dto.setCheckPassword("password1!");
        dto.setWordCount(0);

        Set<ConstraintViolation<SignUpRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        assertTrue(dto.getWordCount() > 0, "wordCount는 0보다 커야 합니다.");
    }

}
