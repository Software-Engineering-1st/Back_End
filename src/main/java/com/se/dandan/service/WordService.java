package com.se.dandan.service;

import com.se.dandan.entity.Member;
import com.se.dandan.entity.Word;
import com.se.dandan.entity.WordBook;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.repository.WordBookRepository;
import com.se.dandan.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WordService {

    private final WordRepository wordRepository;

    private final MemberRepository memberRepository;

    private final WordBookRepository wordBookRepository;

    public WordService(WordRepository wordRepository, MemberRepository memberRepository,  WordBookRepository wordBookRepository) {
        this.wordRepository = wordRepository;
        this.memberRepository = memberRepository;
        this.wordBookRepository = wordBookRepository;
    }

    public List<Word> getTodayWords(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        int wordCount = member.getWordCount();

        List<Word> allWords = wordRepository.findAll();
        Collections.shuffle(allWords);
        List<Word> words = allWords.stream().limit(wordCount).toList();

        words.forEach(word -> {
            wordBookRepository.save(WordBook.builder()
                            .member(member)
                            .word(word)
                            .isMemorized(false)
                            .isTested(false)
                    .build());
        });

        return words;
    }

    public Word findById(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.WORD_NOT_FOUND));

        return word;
    }

    public void markedMemorized(Long memberId, Long wordId) {
        WordBook wordBook = wordBookRepository.findByMemberId(memberId).stream()
                .filter(w -> w.getWord().getId().equals(wordId))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.WORDBOOK_NOT_FOUND));

        wordBook.setMemorized(true);
        wordBookRepository.save(wordBook);
    }
}
