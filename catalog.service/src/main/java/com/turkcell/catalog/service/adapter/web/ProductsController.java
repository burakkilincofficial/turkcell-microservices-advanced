package com.turkcell.catalog.service.adapter.web;

import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import com.turkcell.catalog.service.domain.ProductId;
import com.turkcell.catalog.service.generated.api.ProductsApi;
import com.turkcell.catalog.service.generated.model.WebProductCreateRequest;
import com.turkcell.catalog.service.generated.model.WebProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
public class ProductsController implements ProductsApi
{
    private final ProductUseCase productUseCase;

    public ProductsController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @Override
    public ResponseEntity<WebProductResponse> getProductById(UUID id) {
        var response = productUseCase.getById(id);

        WebProductResponse webProductResponse = new WebProductResponse();
        webProductResponse.setId(response.id().value());
        webProductResponse.setName(response.name());
        webProductResponse.stock(new BigDecimal(response.stock()));

        return ResponseEntity.ok(webProductResponse);
    }

    @Override
    public ResponseEntity<WebProductResponse> createProduct(WebProductCreateRequest productCreateRequest) {
        System.out.println("İstek işleniyor..");
        var command = new ProductUseCase.CreateProductCommand(
                productCreateRequest.getName(),
                productCreateRequest.getDescription(),
                productCreateRequest.getPrice(),
                productCreateRequest.getCurrency(),
                productCreateRequest.getStock().intValue()
        );

        var product = productUseCase.createProduct(command);

        WebProductResponse productResponse = new WebProductResponse();
        productResponse.id(product.id().value());
        productResponse.name(product.name());
        productResponse.description(product.description());
        productResponse.currency(product.price().currency());
        productResponse.price(product.price().amount());
        productResponse.stock(new BigDecimal(product.stock()));

        return ResponseEntity.created(URI.create("")).body(productResponse);
    }
}
