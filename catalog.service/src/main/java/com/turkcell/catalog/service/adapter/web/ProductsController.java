package com.turkcell.catalog.service.adapter.web;

import brave.Tracer;
import com.turkcell.catalog.service.application.port.in.ProductUseCase;
import com.turkcell.catalog.service.domain.ProductId;
import com.turkcell.catalog.service.generated.api.ProductsApi;
import com.turkcell.catalog.service.generated.model.WebProductCreateRequest;
import com.turkcell.catalog.service.generated.model.WebProductResponse;
import io.micrometer.observation.annotation.Observed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
@Observed(name="products-controller")
public class ProductsController implements ProductsApi
{
    private final ProductUseCase productUseCase;
    private final Tracer tracer;

    public ProductsController(ProductUseCase productUseCase, Tracer tracer) {
        this.productUseCase = productUseCase;
        this.tracer = tracer;
    }

    @GetMapping("me/{username}")
    @PreAuthorize("hasAuthority('ADMIN') and #username == authentication.token.claims['preferred_username']")
    public String me(@PathVariable String username) {
        return "";
    }

    @GetMapping("me")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Observed(name="products-controller.me")
    public String me(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt.getClaim("preferred_username").toString());
        return "";
    }

    @Override
    @Observed(name="products-controller.get-product-by-id")
    public ResponseEntity<WebProductResponse> getProductById(UUID id) {
        var span = tracer.currentSpan();
        if(span!=null)
        {
            span.tag("id", id.toString());
        }

        var response = productUseCase.getById(id);

        WebProductResponse webProductResponse = new WebProductResponse();
        webProductResponse.setId(response.id().value());
        webProductResponse.setName(response.name());
        webProductResponse.stock(new BigDecimal(response.stock()));
        webProductResponse.price(response.price().amount());
        webProductResponse.description(response.description());
        webProductResponse.currency(response.price().currency());

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
