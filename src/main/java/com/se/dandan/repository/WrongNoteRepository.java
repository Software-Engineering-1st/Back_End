package com.se.dandan.repository;

import com.se.dandan.entity.WrongNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WrongNoteRepository extends JpaRepository<WrongNote, Long> {
    List<WrongNote> findByMemberIdAndTestDate(Long memberId, LocalDate testDate);

    @Query("SELECT DISTINCT w.testDate FROM WrongNote w WHERE w.member.id = :memberId ORDER BY w.testDate DESC")
    List<LocalDate> findDistinctTestDatesByMemberId(@Param("memberId") Long memberId);
}
