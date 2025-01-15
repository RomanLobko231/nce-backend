package com.nce.backend.cars.domain.events;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record NewCarSavedEvent(UUID id, String registrationNumber) {
}
