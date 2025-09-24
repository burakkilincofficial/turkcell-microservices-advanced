package com.turkcell.catalog.service.adapter.messaging;

import java.util.List;
import java.util.UUID;

public class OrderCreatedPayload {
    public UUID orderId; public UUID customerId; public String status;
    public List<Item> items;
    public static class Item { public UUID productId; public int quantity; }
}