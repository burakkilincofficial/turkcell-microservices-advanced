package com.turkcell.catalog.service.controller;

import com.turkcell.catalog.service.generated.api.ProductsApi;
import com.turkcell.catalog.service.generated.model.Product;
import com.turkcell.catalog.service.generated.model.ProductCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController implements ProductsApi {

    @Override
    public ResponseEntity<Product> createProduct(ProductCreateRequest productCreateRequest) {
        return ProductsApi.super.createProduct(productCreateRequest);
    }
}
