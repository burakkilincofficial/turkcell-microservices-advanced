package com.turkcell.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name="catalog-service", path="/api/v1/products")
public interface CatalogClient
{
    @GetMapping("/{id}")
    GetByIdProductResponse getProductById(@PathVariable("id") UUID id);


    record GetByIdProductResponse(UUID id, String name, String description, int stock, BigDecimal price, String currency) {}
}
