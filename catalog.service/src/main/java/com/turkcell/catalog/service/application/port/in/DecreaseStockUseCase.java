package com.turkcell.catalog.service.application.port.in;

import java.util.List;
import java.util.UUID;

public interface DecreaseStockUseCase {
    void handle(DecreaseStockCommand cmd);

    public record DecreaseStockCommand(UUID eventId, String eventType, UUID orderId, List<Item> items) { public record Item(UUID productId, int quantity) {} }
}
