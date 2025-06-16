package com.nce.backend.logging.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoggingRepository {
    LogEntry save(LogEntry entry);
    List<LogEntry> findAll();
    Optional<LogEntry> findById(UUID id);
}
