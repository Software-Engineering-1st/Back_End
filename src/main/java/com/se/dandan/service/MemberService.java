package com.se.dandan.service;

import com.se.dandan.dto.MemberInfoDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
