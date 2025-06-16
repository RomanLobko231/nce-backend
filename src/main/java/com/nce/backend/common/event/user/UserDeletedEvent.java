package com.nce.backend.common.event.user;

import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
public record UserDeletedEvent(UUID id){
}
