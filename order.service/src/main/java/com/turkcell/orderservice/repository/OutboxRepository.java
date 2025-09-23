package com.turkcell.orderservice.repository;

import com.turkcell.orderservice.outbox.OutboxMessage;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxMessage, UUID>
{
    @Query(
            "Select o from OutboxMessage o Where o.status = 'PENDING' order by o.createdAt LIMIT :limit"
    )
    List<OutboxMessage> findBatchPending(@Param("limit") int limit);

    Optional<OutboxMessage> findByEventId(UUID eventId);
}
