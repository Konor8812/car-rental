package com.illia.carrental.core.model.dto.request;

import java.math.BigDecimal;

public record CreateReservationRequest(Long carId,
                                       Integer numOfDays,
                                       BigDecimal rate,
                                       BigDecimal total) {
}
