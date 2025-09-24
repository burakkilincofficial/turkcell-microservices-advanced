package com.turkcell.catalog.service.application.port.out;

import com.turkcell.catalog.service.domain.ProcessedEvent;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedEventRepository
{
    Optional<ProcessedEvent> getProcessedEventByEventId(UUID eventId);
    ProcessedEvent save(UUID eventId, String eventType);
}
