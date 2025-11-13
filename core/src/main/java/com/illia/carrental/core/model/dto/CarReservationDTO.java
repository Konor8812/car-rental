package com.illia.carrental.core.model.dto;

import java.time.Instant;

public record CarReservationDTO(Long id,
                                CarDTO car,
                                String status,
                                Instant createdAt,
                                Instant validUntil) {
}
