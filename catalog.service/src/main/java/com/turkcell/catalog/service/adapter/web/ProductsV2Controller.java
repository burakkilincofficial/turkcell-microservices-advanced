package com.turkcell.catalog.service.adapter.web;

import com.turkcell.catalog.service.generated.api.ProductsV2Api;
import com.turkcell.catalog.service.generated.model.WebProductCreateRequest;
import com.turkcell.catalog.service.generated.model.WebProductCreateRequestV2;
import com.turkcell.catalog.service.generated.model.WebProductResponse;
import com.turkcell.catalog.service.generated.model.WebProductResponseV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsV2Controller implements ProductsV2Api {
    @Override
    public ResponseEntity<WebProductResponseV2> createProduct(WebProductCreateRequestV2 webProductCreateRequestV2) {
        return ProductsV2Api.super.createProduct(webProductCreateRequestV2);
    }
}
