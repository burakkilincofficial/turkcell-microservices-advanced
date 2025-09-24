package com.turkcell.catalog.service.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record ProcessedEventId(UUID value) implements Serializable {
    public ProcessedEventId {
        Objects.requireNonNull(value, "ProductId.value cannot be null");
    }

    public static ProcessedEventId newId() { return new ProcessedEventId(UUID.randomUUID()); }

    @Override
    public String toString() {
        return value.toString();
    }
}
