package com.se.dandan.service;

import com.se.dandan.dto.MemberInfoDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberService(MemberRepository memberRepository,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public MemberInfoDTO loadMemberInfo(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberInfoDTO.builder()
                .nickname(member.getNickname())
                .userId(member.getUserId())
                .password(bCryptPasswordEncoder.encode(member.getPassword()))
                .build();
    }

    public void updateMemberInfo(String userId, MemberInfoDTO memberInfoDTO) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberInfoDTO> list = new ArrayList<>();
        list.add(memberInfoDTO);

        for(MemberInfoDTO info : list) {
            String nickname = info.getNickname();
            String password = info.getPassword();

            if(nickname != null) {
                if(nickname.length() < 4 || nickname.length() > 6) {
                    throw new CustomException(ErrorCode.VALIDATION_FAIL);
                }
                member.setNickname(nickname);
            }

            if(password != null) {
                if(password.isEmpty() || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")) {
                    throw new CustomException(ErrorCode.VALIDATION_FAIL);
                }
                member.setPassword(bCryptPasswordEncoder.encode(password));
            }
        }

        memberRepository.save(member);
    }
}
