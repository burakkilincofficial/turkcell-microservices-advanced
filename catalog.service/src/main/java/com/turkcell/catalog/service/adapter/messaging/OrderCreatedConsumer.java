package com.turkcell.catalog.service.adapter.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.catalog.service.application.port.in.DecreaseStockUseCase;
import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
public class OrderCreatedConsumer {
    private final ObjectMapper om;
    private final DecreaseStockUseCase useCase;

    public OrderCreatedConsumer(ObjectMapper om, DecreaseStockUseCase useCase) {
        this.om = om;
        this.useCase = useCase;
    }

    @Bean
    public Consumer<Message<String>> orderCreatedConsumerFunction() {
        return msg -> {
            var eventIdStr = (String) msg.getHeaders().get("eventId");
            if (eventIdStr == null) return;
            UUID eventId = UUID.fromString(eventIdStr);
            String eventType = (String) msg.getHeaders().getOrDefault("eventType","OrderCreated");

            OrderCreatedPayload p;
            try { p = om.readValue(msg.getPayload(), OrderCreatedPayload.class); }
            catch (Exception e) { throw new RuntimeException(e); }

            var items = p.items.stream()
                    .map(i -> new DecreaseStockUseCase.DecreaseStockCommand.Item(i.productId, i.quantity))
                    .toList();

            useCase.handle(new DecreaseStockUseCase.DecreaseStockCommand(eventId, eventType, p.orderId, items));
        };
    }
}
