package com.turkcell.catalog.service.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ProcessedEvent
{
    private ProcessedEventId id;
    private UUID eventId;
    private String eventType;
    private OffsetDateTime processedAt;

    public ProcessedEvent(UUID eventId, String eventType, OffsetDateTime processedAt) {
        this.id = new ProcessedEventId(UUID.randomUUID());
        this.eventId = eventId;
        this.eventType = eventType;
        this.processedAt = processedAt;
    }

    public ProcessedEvent(ProcessedEventId id, UUID eventId, String eventType, OffsetDateTime processedAt) {
        this.id = id;
        this.eventId = eventId;
        this.eventType = eventType;
        this.processedAt = processedAt;
    }

    public ProcessedEventId id() {
        return id;
    }

    public ProcessedEvent setId(ProcessedEventId id) {
        this.id = id;
        return this;
    }

    public UUID eventId() {
        return eventId;
    }

    public ProcessedEvent setEventId(UUID eventId) {
        this.eventId = eventId;
        return this;
    }

    public OffsetDateTime processedAt() {
        return processedAt;
    }

    public ProcessedEvent setProcessedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
        return this;
    }

    public String eventType() {
        return eventType;
    }

    public ProcessedEvent setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }
}
