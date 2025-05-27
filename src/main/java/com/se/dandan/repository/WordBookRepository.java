package com.se.dandan.repository;

import com.se.dandan.entity.WordBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {

    List<WordBook> findByMemberId(Long memberId);

    List<WordBook> findByMemberIdAndIsMemorizedTrue(Long memberId);
}
