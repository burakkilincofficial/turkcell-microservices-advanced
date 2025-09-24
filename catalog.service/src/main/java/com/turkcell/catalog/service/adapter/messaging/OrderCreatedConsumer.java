package com.turkcell.catalog.service.adapter.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class OrderCreatedConsumer {


    @Bean
    public Consumer<Message<String>> orderCreatedConsumerFunction() {
        return message -> {
            var eventId = message.getHeaders().get("eventId");
            System.out.println(eventId  + " yakalandı");
            //throw new RuntimeException("Deneme");
        };
    }
}
