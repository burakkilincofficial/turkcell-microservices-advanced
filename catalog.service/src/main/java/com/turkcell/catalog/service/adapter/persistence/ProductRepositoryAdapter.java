package com.turkcell.catalog.service.adapter.persistence;

import com.turkcell.catalog.service.application.port.out.ProductRepository;
import com.turkcell.catalog.service.domain.Money;
import com.turkcell.catalog.service.domain.Product;
import com.turkcell.catalog.service.domain.ProductId;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryAdapter  implements ProductRepository
{
    private final SpringDataProductRepository springDataProductRepository;

    public ProductRepositoryAdapter(SpringDataProductRepository springDataProductRepository) {
        this.springDataProductRepository = springDataProductRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.id().value());
        productEntity.setPrice(product.price().amount());
        productEntity.setCurrency(product.price().currency());
        productEntity.setDescription(product.description());
        productEntity.setName(product.name());
        productEntity.setStock(product.stock());

        productEntity = springDataProductRepository.save(productEntity);

        Product productResponse = new Product(
                new ProductId(productEntity.getId()),
                productEntity.getName(),
                productEntity.getDescription(),
                new Money(productEntity.getPrice(), productEntity.getCurrency()),
                productEntity.getStock(),
                null,
                null
        );
        return productResponse;
    }
}
