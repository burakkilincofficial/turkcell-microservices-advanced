package com.turkcell.catalog.service.application.port.out;

import com.turkcell.catalog.service.domain.Product;

import java.util.Optional;
import java.util.UUID;

// Hizmetleri sunabilmem için aşağıdaki kuralları uygulayan bir sarmallayıcı vermelisin.
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> getById(UUID id);
}
