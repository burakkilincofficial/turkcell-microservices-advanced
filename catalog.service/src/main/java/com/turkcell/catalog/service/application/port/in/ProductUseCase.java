package com.turkcell.catalog.service.application.port.in;

import com.turkcell.catalog.service.domain.Product;

import java.math.BigDecimal;
import java.util.UUID;

// Ben bu işleri yapabilme kabiliyetine sahibim.
// Dış dünyaya : "Ben bu hizmetleri sunuyorum"
public interface ProductUseCase {
    Product createProduct(CreateProductCommand command);
    Product getById(UUID id);

    record CreateProductCommand(String name, String description, BigDecimal price, String currency, int stock){}
}
