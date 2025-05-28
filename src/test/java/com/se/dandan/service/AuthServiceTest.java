package com.se.dandan.service;

import com.se.dandan.dto.IdCheckRequestDTO;
import com.se.dandan.dto.SignInRequestDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.entity.MemberRole;
import com.se.dandan.exception.CustomException;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.util.JWTProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        long refreshedMS = 3600000L;
        authService = new AuthService(memberRepository,
                bCryptPasswordEncoder,
                jwtProvider,
                tokenBlacklistService,
                refreshedMS
        );
    }

    @DisplayName("중복된 아이디가 있으면 예외를 발생해야한다.")
    @Test
    void 아이디_중복확인_중복_예외() {

        // given
        IdCheckRequestDTO idCheckRequestDTO = new IdCheckRequestDTO();
        idCheckRequestDTO.setUserId("test123");

        when(memberRepository.existsByUserId("test123")).thenReturn(true);

        // when & then
        assertThrows(CustomException.class, () -> authService.idCheck(idCheckRequestDTO));
    }

    @Test
    void 로그인_비밀번호_불일치_예외() {
        SignInRequestDTO dto = new SignInRequestDTO();
        dto.setUserId("user");
        dto.setPassword("wrong");

        Member member = Member.builder()
                .userId("user")
                .password("encodedPass")
                .role(MemberRole.MEMBER)
                .build();

        given(memberRepository.findByUserId("user")).willReturn(Optional.of(member));
        given(bCryptPasswordEncoder.matches("wrong", "encodedPass")).willReturn(false);

        assertThrows(CustomException.class, () -> authService.signIn(dto, mock(HttpServletResponse.class)));
    }

}
