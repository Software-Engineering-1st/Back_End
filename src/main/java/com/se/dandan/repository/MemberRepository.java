package com.se.dandan.repository;

import com.se.dandan.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<Member> findById(@NonNull Long memberId);
}
