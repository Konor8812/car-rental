package com.illia.carrental.core.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RentalDetails(BigDecimal rate,
                            Instant availableSince,
                            Instant availableTo) {
}
