package com.se.dandan.service;

import com.se.dandan.dto.MemberPrincipalDTO;
import com.se.dandan.dto.WordDTO;
import com.se.dandan.entity.Member;
import com.se.dandan.entity.Word;
import com.se.dandan.entity.WordBook;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import com.se.dandan.repository.WordBookRepository;
import com.se.dandan.repository.WordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Page<Word> getList(int page) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if (!role.equals("ROLE_ADMIN")) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
        Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());

        return wordRepository.findAll(pageable);
    }

    public Word createWord(WordDTO wordDTO) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if(!role.equals("ROLE_ADMIN")) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }

        String english = wordDTO.getEnglish();
        String meaning = wordDTO.getMeaning1();
        String meaning2 = wordDTO.getMeaning2();
        String meaning3 = wordDTO.getMeaning3();

        Word word = Word.builder()
                .english(english)
                .meaning1(meaning)
                .meaning2(meaning2)
                .meaning3(meaning3)
                .build();

        wordRepository.save(word);
        return word;
    }

    public void updateWord(Long wordId, WordDTO wordDTO) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if(!role.equals("ROLE_ADMIN")) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }

        Word word = wordRepository.findById(wordId).orElseThrow(() -> new CustomException(ErrorCode.WORD_NOT_FOUND));

        List<WordDTO> list = new ArrayList<>();
        list.add(wordDTO);

        for(WordDTO info : list) {
            String english = info.getEnglish();
            String meaning1 = info.getMeaning1();
            String meaning2 = info.getMeaning2();
            String meaning3 = info.getMeaning3();

            if(english != null) {
                word.setEnglish(english);
            }

            if(meaning1 != null) {
                word.setMeaning1(meaning1);
            }

            if(meaning2 != null) {
                word.setMeaning2(meaning2);
            }

            if(meaning3 != null) {
                word.setMeaning3(meaning3);
            }
        }

        wordRepository.save(word);
    }

    public void deleteWord(Long wordId) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if(!role.equals("ROLE_ADMIN")) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
        Word word = wordRepository.findById(wordId).orElseThrow(() -> new CustomException(ErrorCode.WORD_NOT_FOUND));
        wordRepository.delete(word);
    }
}
