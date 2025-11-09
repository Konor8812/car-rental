package com.illia.carrental.core.service;

public interface PaymentService {

    String createPaymentLink(Long userAccountId, Long itemId);
}
