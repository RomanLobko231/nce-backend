package com.nce.backend.logging.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class LogEntry {
    private UUID id;

    private String action;

    private String methodName;

    private Instant logTimestamp;

    private String username;

    private UUID userId;

    private UUID affectedId;
}
