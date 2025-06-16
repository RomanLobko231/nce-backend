package com.nce.backend.logging.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.Instant;
import java.util.UUID;

@Table(name = "app_log")
@Entity(name = "app_log")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogJpaEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String methodName;

    @Column(nullable = false)
    private Instant logTimestamp;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private UUID userId;

    @Column(nullable = true)
    private UUID affectedId;
}
