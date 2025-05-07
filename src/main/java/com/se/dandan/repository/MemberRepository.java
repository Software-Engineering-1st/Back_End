package com.se.dandan.repository;

import com.se.dandan.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByNickname(String nickname);

    Member findByUserId(String userId);

    boolean existsByUserId(String userId);
}
