package com.illia.carrental.payments.dto.request;

public record CompletePaymentRequest(String externalPaymentId,
                                     Boolean isSuccessful) {
}
