package com.nce.backend.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,

        String message,

        LocalDateTime timestamp
) {
    public ErrorResponse(int statusCode, String message) {
        this(statusCode, message, LocalDateTime.now());
    }
}
