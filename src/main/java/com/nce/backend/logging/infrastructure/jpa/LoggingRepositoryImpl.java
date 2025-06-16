package com.nce.backend.logging.infrastructure.jpa;

import com.nce.backend.logging.domain.LogEntry;
import com.nce.backend.logging.domain.LoggingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@Primary
@RequiredArgsConstructor
public class LoggingRepositoryImpl implements LoggingRepository {

    private final LoggingJpaRepository jpaRepository;
    private final LogJpaEntryMapper mapper;

    @Override
    public LogEntry save(LogEntry entry) {
        LogJpaEntry jpaEntity = mapper.toJpaEntity(entry);
        LogJpaEntry savedEntity = jpaRepository.save(jpaEntity);

        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public List<LogEntry> findAll() {
        return jpaRepository
                .findAll()
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<LogEntry> findById(UUID id) {
        return jpaRepository
                .findById(id)
                .map(mapper::toDomainEntity);
    }
}
