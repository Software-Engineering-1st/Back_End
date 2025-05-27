package com.se.dandan.service;

import com.se.dandan.dto.TestQuestionDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.entity.Word;
import com.se.dandan.entity.WordBook;
import com.se.dandan.entity.WrongNote;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.repository.WordBookRepository;
import com.se.dandan.repository.WordRepository;
import com.se.dandan.repository.WrongNoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final WordBookRepository wordBookRepository;
    private final WrongNoteRepository wrongNoteRepository;
    private final MemberRepository memberRepository;
    private final WordRepository wordRepository;

    public List<TestQuestionDTO> generateTestQuestions(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<WordBook> memorizedWords = wordBookRepository.findByMemberIdAndIsMemorizedTrue(memberId);

        // 목표 단어 개수만큼 랜덤 추출
        int count = Math.min(member.getWordCount(), memorizedWords.size());

        Collections.shuffle(memorizedWords);

        List<TestQuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Word word = memorizedWords.get(i).getWord();
            questions.add(TestQuestionDTO.builder()
                    .wordId(word.getId())
                    .meaning1(word.getMeaning1())
                    .meaning2(word.getMeaning2())
                    .meaning3(word.getMeaning3())
                    .build());
        }

        return questions;
    }

    @Transactional
    public boolean checkAnswerAndSaveWrongNote(Long memberId, Long wordId, String userAnswer) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new RuntimeException("단어가 없습니다."));

        boolean isCorrect = word.getEnglish().equalsIgnoreCase(userAnswer.trim());

        if (!isCorrect) {
            // 오답 노트 저장
            WrongNote wrongNote = WrongNote.builder()
                    .member(Member.builder().id(memberId).build())
                    .word(word)
                    .testDate(LocalDate.now())
                    .build();

            wrongNoteRepository.save(wrongNote);
        }

        return isCorrect;
    }
}
