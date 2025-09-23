package com.turkcell.catalog.service.adapter.web;

import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import com.turkcell.catalog.service.generated.api.ProductsApi;
import com.turkcell.catalog.service.generated.model.WebProductCreateRequest;
import com.turkcell.catalog.service.generated.model.WebProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;

@RestController
public class ProductsController implements ProductsApi
{
    private final ProductUseCase productUseCase;

    public ProductsController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @Override
    public ResponseEntity<WebProductResponse> createProduct(WebProductCreateRequest productCreateRequest) {
        System.out.println("İstek işleniyor..");
        var command = new ProductUseCase.CreateProductCommand(
                productCreateRequest.getName(),
                productCreateRequest.getDescription(),
                productCreateRequest.getPrice(),
                productCreateRequest.getCurrency()
        );

        var product = productUseCase.createProduct(command);

        WebProductResponse productResponse = new WebProductResponse();
        productResponse.name(product.name());
        productResponse.description(product.description());
        productResponse.currency(product.price().currency());
        productResponse.price(product.price().amount());

        return ResponseEntity.created(URI.create("")).body(productResponse);
    }
}
