package com.epam.clientinterface.domain.event;

import lombok.Getter;

@Getter
public abstract class DomainEvent {
    private final long occurredOn;

    public DomainEvent() {
        this.occurredOn = System.currentTimeMillis();
    }
}
