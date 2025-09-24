package com.turkcell.catalog.service.application.service;

import com.turkcell.catalog.service.application.port.in.DecreaseStockUseCase;
import com.turkcell.catalog.service.application.port.out.ProcessedEventRepository;
import com.turkcell.catalog.service.application.port.out.ProductRepository;
import com.turkcell.catalog.service.domain.ProcessedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService implements DecreaseStockUseCase {
    private final ProductRepository productRepo;
    private final ProcessedEventRepository processedRepo;

    public InventoryService(ProductRepository productRepo, ProcessedEventRepository processedRepo) {
        this.productRepo = productRepo;
        this.processedRepo = processedRepo;
    }

    @Transactional
    public void handle(DecreaseStockCommand cmd) {
        if (processedRepo.getProcessedEventByEventId(cmd.eventId()).isPresent()) return; // idempotent no-op

        for (var it : cmd.items()) {
            var product = productRepo.getById(it.productId())
                    .orElseThrow(() -> new IllegalStateException("Product not found: " + it.productId()));
            int remaining = product.stock() - it.quantity();
            product.restock(remaining);
            productRepo.save(product);
        }
        processedRepo.save(cmd.eventId(), cmd.eventType());
    }
}