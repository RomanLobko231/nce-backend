package com.nce.backend.common.events.car;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record NewCarSavedEvent(UUID carId, UUID ownerId) {}
