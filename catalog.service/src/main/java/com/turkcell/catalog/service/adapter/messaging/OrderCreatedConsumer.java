package com.turkcell.catalog.service.adapter.messaging;

import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class OrderCreatedConsumer {
    private ProductUseCase productUseCase;

    public OrderCreatedConsumer(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @Bean
    public Consumer<Message<String>> orderCreatedConsumerFunction() {
        return message -> {
            var eventId = message.getHeaders().get("eventId");
            System.out.println(eventId  + " yakalandı");
            //this.productUseCase.decreaseStock(eventId, );
            //throw new RuntimeException("Deneme");
        };
    }
}
