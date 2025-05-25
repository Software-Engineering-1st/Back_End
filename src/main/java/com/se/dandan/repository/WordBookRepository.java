package com.se.dandan.repository;

import com.se.dandan.entity.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {

    List<WordBook> findByMemberId(Long memberId);

    @Query(value = "SELECT * FROM member_word mw " +
            "WHERE mw.member_id = :memberId " +
            "LIMIT :limit", nativeQuery = true)

    List<WordBook> findTodayWords(@Param("memberId") Long memberId, @Param("limit") int limit);
}
