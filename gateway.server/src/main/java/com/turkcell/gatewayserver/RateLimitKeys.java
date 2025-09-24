package com.turkcell.gatewayserver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.mapping.KeySpaceResolver;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitKeys {
    // IP Bazlı Çözümleyici
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }
}
