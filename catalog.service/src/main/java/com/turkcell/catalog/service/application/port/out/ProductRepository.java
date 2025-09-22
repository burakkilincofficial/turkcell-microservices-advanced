package com.turkcell.catalog.service.application.port.out;

import com.turkcell.catalog.service.domain.Product;

// Hizmetleri sunabilmem için aşağıdaki kuralları uygulayan bir sarmallayıcı vermelisin.
public interface ProductRepository {
    Product save(Product product);
}
