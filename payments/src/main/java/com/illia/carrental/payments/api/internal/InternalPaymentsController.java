package com.illia.carrental.payments.api.internal;

import com.illia.carrental.payments.dto.request.CompletePaymentRequest;
import com.illia.carrental.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class InternalPaymentsController {

    private final PaymentsService paymentsService;

    // webhook called by payment processing system
    @PostMapping("/complete")
    public ResponseEntity<Void> completePayment(@RequestBody CompletePaymentRequest completePaymentRequest) {
        paymentsService.completePayment(completePaymentRequest);
        return ResponseEntity.noContent().build();
    }
}
