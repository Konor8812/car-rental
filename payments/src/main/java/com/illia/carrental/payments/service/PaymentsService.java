package com.illia.carrental.payments.service;

import com.illia.carrental.payments.dto.request.CompletePaymentRequest;
import com.illia.carrental.payments.dto.request.CreatePaymentLinkRequest;
import com.illia.carrental.payments.dto.response.CreatePaymentLinkResponse;

public interface PaymentsService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest createPaymentLinkRequest);

    void completePayment(CompletePaymentRequest completePaymentRequest);
}
