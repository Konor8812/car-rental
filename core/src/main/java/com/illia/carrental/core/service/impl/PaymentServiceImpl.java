package com.illia.carrental.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.exception.ReservationException;
import com.illia.carrental.core.config.PaymentConfig;
import com.illia.carrental.core.model.dto.request.CreatePaymentLinkRequestBody;
import com.illia.carrental.core.model.dto.response.CreatePaymentLinkResponse;
import com.illia.carrental.core.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final HttpClient httpClient;
    private final PaymentConfig paymentConfig;
    private final ObjectMapper objectMapper;

    @Override
    public String createPaymentLink(Long userId, Long reservationId, BigDecimal total) {
        try {
            var body = createBody(userId, reservationId, total);
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(paymentConfig.getCreatePaymentLinkUrl()))
                    .header("Content-Type", "application/json")
                    .header("client-secret", paymentConfig.clientSecret())
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var parsed = objectMapper.readValue(response.body(), CreatePaymentLinkResponse.class);
            return parsed.link();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReservationException("Was not able to create payment link");
        }
    }

    private String createBody(Long userId, Long reservationId, BigDecimal total) throws Exception {
        var requestBody = new CreatePaymentLinkRequestBody(userId, reservationId, total);
        return objectMapper.writeValueAsString(requestBody);
    }
}
