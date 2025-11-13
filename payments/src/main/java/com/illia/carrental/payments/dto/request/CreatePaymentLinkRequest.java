package com.illia.carrental.payments.dto.request;

import java.math.BigDecimal;

public record CreatePaymentLinkRequest(Long userId,
                                       Long reservationId,
                                       BigDecimal total) {
}
