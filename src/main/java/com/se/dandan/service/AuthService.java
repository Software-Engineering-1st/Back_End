package com.se.dandan.service;

import com.se.dandan.dto.IdCheckRequestDTO;
import com.se.dandan.dto.SignUpRequestDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.entity.MemberRole;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.util.JWTProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
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
}
