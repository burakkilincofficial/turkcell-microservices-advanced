package com.turkcell.orderservice.outbox;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="outbox", indexes = {
        @Index(name="ix_outbox_event_id", columnList ="eventId", unique = true),
        @Index(name="ix_outbox_status_created", columnList = "status, createdAt")
})
public class OutboxMessage
{
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID eventId = UUID.randomUUID(); // idempotency
    @Column(nullable = false)
    private String aggregateType;
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID aggregateId;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String payloadJson;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    private int retryCount = 0;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime processedAt;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(OffsetDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
