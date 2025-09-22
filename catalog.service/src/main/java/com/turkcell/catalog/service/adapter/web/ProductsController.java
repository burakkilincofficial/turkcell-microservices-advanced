package com.turkcell.catalog.service.adapter.web;

import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import com.turkcell.catalog.service.generated.api.ProductsApi;
import com.turkcell.catalog.service.generated.model.Product;
import com.turkcell.catalog.service.generated.model.ProductCreateRequest;
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
    public ResponseEntity<Product> createProduct(ProductCreateRequest productCreateRequest) {
        var command = new ProductUseCase.CreateProductCommand(
                productCreateRequest.getName(),
                productCreateRequest.getDescription(),
                new BigDecimal(300),
                "TRY"
        );

        var product = productUseCase.createProduct(command);

        Product productResponse = new Product();
        productResponse.name(product.name());
        productResponse.description(product.description());

        return ResponseEntity.created(URI.create("")).body(productResponse);
    }
}
