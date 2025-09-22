package com.turkcell.catalog.service.domain;

import java.math.BigDecimal;
import java.util.Objects;

// Bu bir value objecttir. Değişmez (Immutable) olmalıdır.
// Java 16+ -> record'da alanlar final ->
// equals/hashCode/toString  -> ValueObject'te değer eşitliği esas.
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        Objects.requireNonNull(currency, "Currency cannot be null");
    }
}
// Boilerplate -> Basmakalıp kod fazlalığı azaldı.