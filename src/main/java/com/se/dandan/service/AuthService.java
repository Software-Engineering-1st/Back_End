package com.se.dandan.service;

import com.se.dandan.dto.IdCheckRequestDTO;
import com.se.dandan.dto.SignInRequestDTO;
import com.se.dandan.dto.SignUpRequestDTO;
import com.se.dandan.dto.TokenInfo;
import com.se.dandan.entity.Member;
import com.se.dandan.entity.MemberRole;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.util.JWTProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTProvider jwtProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final long refreshedMS;

    public AuthService(MemberRepository memberRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JWTProvider jwtProvider,
                       TokenBlacklistService tokenBlacklistService,
                       @Value("${jwt.refreshedMs}") long refreshedMS) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshedMS = refreshedMS;
    }

    public void idCheck(IdCheckRequestDTO idCheckRequestDTO) {
        boolean existedUserId = memberRepository.existsByUserId(idCheckRequestDTO.getUserId());
        if (existedUserId) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }
    }

    public void signUp(SignUpRequestDTO signUpRequestDTO) {
        String nickname = signUpRequestDTO.getNickname();

        String userId = signUpRequestDTO.getUserId();
        boolean existedUserId = memberRepository.existsByUserId(userId);

        if (existedUserId) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        String password = signUpRequestDTO.getPassword();
        String checkPassword = signUpRequestDTO.getCheckPassword();

        if(!password.equals(checkPassword)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_PASSWORD);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        signUpRequestDTO.setPassword(encodedPassword);

        int wordCount = signUpRequestDTO.getWordCount();

        Member member = Member.builder()
                .nickname(nickname)
                .userId(userId)
                .password(encodedPassword)
                .wordCount(wordCount)
                .role(MemberRole.MEMBER)
                .build();

        memberRepository.save(member);
    }

    public TokenInfo signIn(SignInRequestDTO signInRequestDTO, HttpServletResponse response) {
        String userId = signInRequestDTO.getUserId();

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String password = signInRequestDTO.getPassword();
        String encodedPassword = member.getPassword();

        boolean isMatched = bCryptPasswordEncoder.matches(password, encodedPassword);
        if(!isMatched) {
            throw new CustomException(ErrorCode.SIGN_IN_FAILED);
        }

        String role = member.getRole().name();

        TokenInfo token = jwtProvider.generateToken(userId, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, role);

        response.addCookie(createCookie(refreshToken));

        return token;
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("Refresh", value);
        cookie.setMaxAge((int)refreshedMS / 1000);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
