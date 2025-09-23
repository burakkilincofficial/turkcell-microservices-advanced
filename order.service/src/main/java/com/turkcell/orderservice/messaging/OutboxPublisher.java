package com.turkcell.orderservice.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class OutboxPublisher {

    @Bean("outboxPublisherSupplier")
    public Supplier<Message<String>> outboxPublisherSupplier() {
        // Spring Cloud Stream'in default verdiği publisherı kullanmıcam.
        return () -> null;
        // Programatik akış istiyorum.
    }

    public Message<String> buildMessage(String payloadJson, String eventType, String eventId)
    {
        return MessageBuilder.withPayload(payloadJson)
                .setHeader("eventType",eventType)
                .setHeader("eventId", eventId)
                .build();
    }
}
