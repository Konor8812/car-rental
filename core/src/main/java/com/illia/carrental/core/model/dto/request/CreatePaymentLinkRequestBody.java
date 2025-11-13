package com.illia.carrental.core.model.dto.request;

import java.math.BigDecimal;

public record CreatePaymentLinkRequestBody(Long userId,
                                           Long reservationId,
                                           BigDecimal total) {
}
