package com.se.dandan.repository;

import com.se.dandan.entity.Word;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT * FROM Word ORDER BY RANDOM() LIMIT :count", nativeQuery = true)

    List<Word> findRandomWords(@Param("count") int count);

    Optional<Word> findById(@NonNull Long wordId);
}
