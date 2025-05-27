package com.se.dandan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WrongNoteDTO {
    private String english;
    private String meaning1;
    private String meaning2;
    private String meaning3;
    private LocalDate testDate;
}
