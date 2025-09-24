package com.turkcell.catalog.service.adapter.persistence.repo;

import com.turkcell.catalog.service.adapter.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {
}
