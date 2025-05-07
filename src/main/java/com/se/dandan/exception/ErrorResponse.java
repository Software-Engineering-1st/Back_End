package com.se.dandan.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String message;

    public ErrorResponse(ErrorCode code) {
        this.status = code.getHttpStatus().value();
        this.message = code.getMessage();
    }
}
