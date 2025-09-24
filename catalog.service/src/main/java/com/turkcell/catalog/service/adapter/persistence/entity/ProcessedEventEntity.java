package com.turkcell.catalog.service.adapter.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="processed_events")
public class ProcessedEventEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID eventId;
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false)
    private OffsetDateTime processedAt = OffsetDateTime.now();

    public UUID id() {
        return id;
    }

    public ProcessedEventEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID eventId() {
        return eventId;
    }

    public ProcessedEventEntity setEventId(UUID eventId) {
        this.eventId = eventId;
        return this;
    }

    public String eventType() {
        return eventType;
    }

    public ProcessedEventEntity setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public OffsetDateTime processedAt() {
        return processedAt;
    }

    public ProcessedEventEntity setProcessedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
        return this;
    }
}
