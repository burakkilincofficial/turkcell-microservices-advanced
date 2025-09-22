package com.turkcell.catalog.service.application.port.in;

import com.turkcell.catalog.service.domain.Product;

import java.math.BigDecimal;

// Ben bu işleri yapabilme kabiliyetine sahibim.
// Dış dünyaya : "Ben bu hizmetleri sunuyorum"
public interface ProductUseCase {
    Product createProduct(CreateProductCommand command);

    record CreateProductCommand(String name, String description, BigDecimal price, String currency){}
}
