package com.nce.backend.common.events;

import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@DomainEvent
public record UserDeletedEvent(UUID id){
}
