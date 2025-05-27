package com.se.dandan.service;

import com.se.dandan.dto.WrongNoteDTO;
import com.se.dandan.entity.WrongNote;
import com.se.dandan.repository.WrongNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongNoteService {

    private final WrongNoteRepository wrongNoteRepository;

    public List<LocalDate> getWrongNoteDates(Long memberId) {
        return wrongNoteRepository.findDistinctTestDatesByMemberId(memberId);
    }

    public List<WrongNoteDTO> getWrongNotesByDate(Long memberId, LocalDate date) {
        List<WrongNote> notes = wrongNoteRepository.findByMemberIdAndTestDate(memberId, date);
        List<WrongNoteDTO> dtos = notes.stream()
                .map(note -> new WrongNoteDTO(
                        note.getWord().getEnglish(),
                        note.getWord().getMeaning1(),
                        note.getWord().getMeaning2(),
                        note.getWord().getMeaning3(),
                        note.getTestDate()
                ))
                .toList();

        return dtos;
    }
}
