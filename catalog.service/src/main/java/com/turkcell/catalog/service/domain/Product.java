package com.turkcell.catalog.service.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

// Aggregate Root
public class Product {
    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Product(String name,
                   String description,
                   Money price,
                   OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
        this.id = new ProductId(UUID.randomUUID());
        rename(name);
        redescribe(description);
        reprice(price);
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    public Product(ProductId id,
                   String name,
                   String description,
                   Money price,
                   OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        rename(name);
        redescribe(description);
        reprice(price);
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : OffsetDateTime.now();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isBlank()) throw new RuntimeException();
        if (name.length() > 255) throw new RuntimeException();
    }

    public void rename(String name) {
        validateName(name);
        this.name = name;
        touch();
    }

    public void redescribe(String description) {
        this.description = description;
        touch();
    }

    public void reprice(Money price) {
        if (price == null) throw new RuntimeException();
        this.price = price;
        touch();
    }

    public void touch() {updatedAt = OffsetDateTime.now();}

    /* Getters */
    public ProductId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Money price() {
        return price;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime updatedAt() {
        return updatedAt;
    }
    /* Getters */
}
