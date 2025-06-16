package com.nce.backend.logging.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggingDomainService {

    private final LoggingRepository loggingRepository;

    @Transactional
    public LogEntry save(LogEntry logEntry) {
        return loggingRepository.save(logEntry);
    }
}
