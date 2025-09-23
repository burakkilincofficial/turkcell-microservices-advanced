package com.turkcell.orderservice.messaging;

import com.turkcell.orderservice.outbox.OutboxMessage;
import com.turkcell.orderservice.outbox.OutboxStatus;
import com.turkcell.orderservice.repository.OutboxRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class OutboxRelayer {
    // Scheduled Job çalıştır, belirlenen aralıkta belirlenen batchSizeda işlem yapıcak.

    private final OutboxRepository outboxRepository;
    private final OutboxPublisher outboxPublisher;
    private final StreamBridge streamBridge;

    private int batchSize = 100;
    private int maxAttempts = 10;

    private static final String BINDING_NAME = "outboxPublisherSupplier-out-0";
    public OutboxRelayer(OutboxRepository outboxRepository, OutboxPublisher outboxPublisher, StreamBridge streamBridge) {
        this.outboxRepository = outboxRepository;
        this.outboxPublisher = outboxPublisher;
        this.streamBridge = streamBridge;
    }
    @Scheduled(fixedDelayString = "${app.outbox.poll.interval-ms:1000}")
    public void pollAndPublish() {
        System.out.println("Polling");
        List<OutboxMessage> messagesToProcess = outboxRepository.findBatchPending(batchSize);

        for (OutboxMessage message : messagesToProcess) {
            try{
                Message<String> m = outboxPublisher.buildMessage(
                        message.getPayloadJson(),
                        message.getType(),
                        message.getEventId().toString()
                );
                boolean sent = streamBridge.send(BINDING_NAME, m);
                if(!sent) throw new RuntimeException("Failed to send message");

                message.setStatus(OutboxStatus.SENT);
                message.setProcessedAt(OffsetDateTime.now());
                outboxRepository.save(message);
            }catch(Exception e) {
                int next = message.getRetryCount() + 1;
                message.setRetryCount(next);

                if(next >= maxAttempts) {
                    message.setStatus(OutboxStatus.FAILED);
                    message.setProcessedAt(OffsetDateTime.now());
                }
                outboxRepository.save(message);
            }
        }
    }
}
