package com.illia.carrental.payments.service.impl;

import com.illia.carrental.payments.data.entity.Payment;
import com.illia.carrental.payments.data.repository.PaymentRepository;
import com.illia.carrental.payments.dto.request.CompletePaymentRequest;
import com.illia.carrental.payments.dto.request.CreatePaymentLinkRequest;
import com.illia.carrental.payments.dto.response.CreatePaymentLinkResponse;
import com.illia.carrental.payments.service.CarRentalCoreNotificator;
import com.illia.carrental.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.illia.carrental.payments.commons.enums.PaymentStatus.*;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentRepository paymentRepository;
    private final CarRentalCoreNotificator coreNotificator;

    @Override
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request) {
        var externalPaymentId = UUID.randomUUID().toString();
        var link = String.format("/payments/%s", externalPaymentId);
        var payment = Payment.builder()
                .userId(request.userId())
                .reservationId(request.reservationId())
                .total(request.total())
                .status(PENDING)
                .paymentLink(link)
                .externalPaymentId(externalPaymentId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        paymentRepository.save(payment);
        return new CreatePaymentLinkResponse(payment.getPaymentLink());
    }

    @Override
    public void completePayment(CompletePaymentRequest request) {
        var payment = paymentRepository.findByExternalPaymentId(request.externalPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(request.isSuccessful() ? SUCCESS : FAILED);
        payment.setUpdatedAt(Instant.now());
        paymentRepository.save(payment);

        if (request.isSuccessful()) {
            coreNotificator.notifyReservationCompleted(payment.getReservationId());
        }
    }
}
