package com.nce.backend.common.events;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record CarSaveFailedRollbackUserEvent(UUID userId) {
}
