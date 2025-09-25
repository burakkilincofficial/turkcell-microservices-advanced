package com.turkcell.orderservice.client;

import io.micrometer.observation.annotation.Observed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name="catalog-service", path="/api/v1/products")
@Observed(name="catalog-client")
public interface CatalogClient
{
    @GetMapping("/{id}")
    @Observed(name="catalog-client.get-product-by-id")
    GetByIdProductResponse getProductById(@PathVariable("id") UUID id);


    record GetByIdProductResponse(UUID id, String name, String description, int stock, BigDecimal price, String currency) {}
}
