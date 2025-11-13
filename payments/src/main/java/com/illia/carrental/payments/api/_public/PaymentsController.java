package com.illia.carrental.payments.api._public;

import com.illia.carrental.payments.dto.request.CreatePaymentLinkRequest;
import com.illia.carrental.payments.dto.response.CreatePaymentLinkResponse;
import com.illia.carrental.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @PostMapping("")
    public ResponseEntity<CreatePaymentLinkResponse> createPaymentLink(@RequestBody CreatePaymentLinkRequest createPaymentLinkRequest) {
        return ResponseEntity.ok(paymentsService.createPaymentLink(createPaymentLinkRequest));
    }
}
