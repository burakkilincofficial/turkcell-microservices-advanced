package com.turkcell.catalog.service.adapter.persistence.repo;

import com.turkcell.catalog.service.adapter.persistence.entity.ProcessedEventEntity;
import com.turkcell.catalog.service.application.port.out.ProcessedEventRepository;
import com.turkcell.catalog.service.domain.ProcessedEvent;
import com.turkcell.catalog.service.domain.ProcessedEventId;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepository {
    private final SpringDataProcessedEventRepository springDataProcessedEventRepository;

    public ProcessedEventRepositoryAdapter(SpringDataProcessedEventRepository springDataProcessedEventRepository) {
        this.springDataProcessedEventRepository = springDataProcessedEventRepository;
    }

    @Override
    public Optional<ProcessedEvent> getProcessedEventByEventId(UUID eventId) {
        Optional<ProcessedEventEntity> opt = springDataProcessedEventRepository
                .findById(eventId);
        if(opt.isPresent())
        {
            ProcessedEventEntity entity = opt.get();
            ProcessedEvent event = new ProcessedEvent(
                    new ProcessedEventId(entity.id()), entity.eventId(), entity.eventType(), entity.processedAt());
            return Optional.of(event);
        }
        return Optional.empty();
    }

    @Override
    public ProcessedEvent save(UUID eventId, String eventType) {
        ProcessedEventEntity entity = new ProcessedEventEntity();
        entity.setEventId(eventId);
        entity.setEventType(eventType);
        entity.setProcessedAt(OffsetDateTime.now());

        entity = springDataProcessedEventRepository.save(entity);

        return new ProcessedEvent(new ProcessedEventId(entity.id()), entity.eventId(), entity.eventType(), entity.processedAt());
    }
}
