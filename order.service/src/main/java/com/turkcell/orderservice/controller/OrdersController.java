package com.turkcell.orderservice.controller;

import com.turkcell.orderservice.client.CatalogClient;
import com.turkcell.orderservice.contract.GetProductByIdResponse;
import com.turkcell.orderservice.dto.CreateOrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    private final RestTemplate restTemplate;
    private final CatalogClient catalogClient;

    public OrdersController(RestTemplate restTemplate, CatalogClient catalogClient) {
        this.restTemplate = restTemplate;
        this.catalogClient = catalogClient;
    }

    @PostMapping
    public String addOrder(@RequestBody List<CreateOrderDto> orders) {
        //....CatalogService'e git her bir ürün için istenen miktarın stokda olup olmadığını kontrol et.
        for (CreateOrderDto order : orders) {
            /*var response = restTemplate
                    .getForEntity("http://localhost:8888/api/v1/products/"+order.getProductId(), GetProductByIdResponse.class);
            if (response.getStatusCode() != HttpStatus.OK  || order.getQuantity() > response.getBody().getStock())
                throw new RuntimeException("Sorry, you don't have enough stock");*/

            var response = catalogClient.getProductById(order.getProductId());
            if (order.getQuantity() > response.stock())
                throw new RuntimeException("Sorry, you don't have enough stock");
        }

        return "Sipariş Başarılı";
    }
}
