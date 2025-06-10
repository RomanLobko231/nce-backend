package com.nce.backend.logging.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoggingJpaRepository extends JpaRepository<LogJpaEntry, UUID> {
}
