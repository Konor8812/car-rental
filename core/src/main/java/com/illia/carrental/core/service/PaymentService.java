package com.illia.carrental.core.service;

import java.math.BigDecimal;

public interface PaymentService {

    String createPaymentLink(Long userAccountId, Long itemId, BigDecimal total);
}
