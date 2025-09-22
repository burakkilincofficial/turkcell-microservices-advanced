package com.turkcell.catalog.service.domain;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {
    public ProductId {
        Objects.requireNonNull(value, "ProductId.value cannot be null");
    }

    public static ProductId newId() { return new ProductId(UUID.randomUUID()); }

    @Override
    public String toString() {
        return value.toString();
    }
}
