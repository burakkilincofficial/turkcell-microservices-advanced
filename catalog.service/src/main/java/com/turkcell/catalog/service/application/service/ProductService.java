package com.turkcell.catalog.service.application.service;

import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import com.turkcell.catalog.service.application.port.out.ProcessedEventRepository;
import com.turkcell.catalog.service.application.port.out.ProductRepository;
import com.turkcell.catalog.service.domain.Money;
import com.turkcell.catalog.service.domain.Product;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService implements ProductUseCase
{
    private final ProductRepository productRepository;
    private final ProcessedEventRepository processedEventRepository;
    public ProductService(ProductRepository productRepository, ProcessedEventRepository processedEventRepository) {
        this.productRepository = productRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Override
    public Product createProduct(CreateProductCommand command) {
        var product = new Product(command.name(),
                command.description(),
                new Money(command.price(),
                command.currency()),
                command.stock(),
                null,
                null);

        return productRepository.save(product);
    }

    @Override
    public Product getById(UUID id) {
        return productRepository
                .getById(id)
                .orElseThrow(() -> new NotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public void decreaseStock(UUID eventId, UUID productId, int quantity) {
        var event = processedEventRepository.getProcessedEventByEventId(eventId);
        if(event.isPresent()) return;

        // ......
    }
}
