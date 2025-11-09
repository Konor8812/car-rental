package com.illia.carrental.core.service.impl;

import com.illia.carrental.core.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String createPaymentLink(Long userAccountId, Long itemId) {
        return "test";
    }
}
