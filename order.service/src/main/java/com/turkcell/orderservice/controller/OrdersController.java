package com.turkcell.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.orderservice.client.CatalogClient;
import com.turkcell.orderservice.contract.GetProductByIdResponse;
import com.turkcell.orderservice.dto.CreateOrderDto;
import com.turkcell.orderservice.entity.Order;
import com.turkcell.orderservice.entity.OrderItem;
import com.turkcell.orderservice.outbox.OutboxMessage;
import com.turkcell.orderservice.repository.OrderItemRepository;
import com.turkcell.orderservice.repository.OrderRepository;
import com.turkcell.orderservice.repository.OutboxRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    private final RestTemplate restTemplate;
    private final CatalogClient catalogClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    public OrdersController(RestTemplate restTemplate, CatalogClient catalogClient, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.restTemplate = restTemplate;
        this.catalogClient = catalogClient;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }


    public record OrderCreatedPayload(UUID orderId, UUID customerId, List<OrderItemPayload> items) {}
    public record OrderItemPayload(UUID productId, int quantity, BigDecimal unitPrice) {}
    @PostMapping
    @Transactional
    public String addOrder(@RequestBody List<CreateOrderDto> orders)
            throws JsonProcessingException {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        for (CreateOrderDto orderDto : orders) {
            var response = catalogClient.getProductById(orderDto.getProductId());
            if (orderDto.getQuantity() > response.stock())
                throw new RuntimeException("Sorry, you don't have enough stock");

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderDto.getProductId());
            orderItem.setQuantity(orderDto.getQuantity());
            orderItem.setUnitPrice(response.price());
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }

        //order.setTotalPrice(order.getItems());
        List<OrdersController.OrderItemPayload> itemPayloads = orderItemRepository
                .findByOrderId(order.getId())
                .stream()
                .map(oi -> new OrdersController.OrderItemPayload(oi.getProductId(), oi.getQuantity(), oi.getUnitPrice()))
                .toList();

        OrdersController.OrderCreatedPayload orderCreatedPayload =
                new OrdersController.OrderCreatedPayload(order.getId(), order.getCustomerId(), itemPayloads);

        OutboxMessage outboxMessage = new OutboxMessage();
        outboxMessage.setAggregateType("Order");
        outboxMessage.setAggregateId(order.getId());
        outboxMessage.setType("OrderCreated");
        outboxMessage.setPayloadJson(objectMapper.writeValueAsString(orderCreatedPayload));
        outboxRepository.save(outboxMessage);
        return "Sipariş Başarılı";
    }

    // TODO: Dto
    @GetMapping
    //@PostAuthorize("returnObject.customerId == authentication.token.claims['sub']")
    public Order getOrder(@AuthenticationPrincipal Jwt jwt) {
        // CustomerId bul..
        // Repository'e bununla filtre gönder.

        Order order = new Order();
        order.setCustomerId(UUID.fromString("361f73a2-8d6f-4a83-9918-0fbf902543bf"));
        return order;
    }
}
