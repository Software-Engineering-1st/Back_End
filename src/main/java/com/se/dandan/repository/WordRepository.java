package com.se.dandan.repository;

import com.se.dandan.entity.Word;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findById(@NonNull Long wordId);
}
