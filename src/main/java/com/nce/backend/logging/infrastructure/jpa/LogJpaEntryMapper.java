package com.nce.backend.logging.infrastructure.jpa;

import com.nce.backend.logging.domain.LogEntry;
import org.springframework.stereotype.Component;

@Component
public class LogJpaEntryMapper {

    LogJpaEntry toJpaEntity(LogEntry logEntry) {
        return LogJpaEntry
                .builder()
                .id(logEntry.getId())
                .username(logEntry.getUsername())
                .action(logEntry.getAction())
                .affectedId(logEntry.getAffectedId())
                .logTimestamp(logEntry.getLogTimestamp())
                .methodName(logEntry.getMethodName())
                .userId(logEntry.getUserId())
                .build();
    }

    LogEntry toDomainEntity(LogJpaEntry jpaEntity){
        return LogEntry
                .builder()
                .id(jpaEntity.getId())
                .username(jpaEntity.getUsername())
                .action(jpaEntity.getAction())
                .affectedId(jpaEntity.getAffectedId())
                .logTimestamp(jpaEntity.getLogTimestamp())
                .methodName(jpaEntity.getMethodName())
                .userId(jpaEntity.getUserId())
                .build();
    }
}
