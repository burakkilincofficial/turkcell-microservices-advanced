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
}
