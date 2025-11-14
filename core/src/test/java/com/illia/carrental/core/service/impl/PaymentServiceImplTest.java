package com.illia.carrental.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.exception.ReservationException;
import com.illia.carrental.core.config.PaymentConfig;
import com.illia.carrental.core.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private HttpClient httpClient;
    private PaymentConfig config;
    private ObjectMapper objectMapper;
    private PaymentService service;

    @BeforeEach
    void setup() {
        httpClient = mock(HttpClient.class);
        config = mock(PaymentConfig.class);
        objectMapper = new ObjectMapper();

        when(config.getCreatePaymentLinkUrl()).thenReturn("http://pay/create");
        when(config.clientSecret()).thenReturn("secret");

        service = new PaymentServiceImpl(httpClient, config, objectMapper);
    }

    @Test
    void testCreatePaymentLink_success() throws Exception {
        String json = """
                {"link": "http://payment.link/123"}
                """;

        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.body()).thenReturn(json);

        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(response);

        String link = service.createPaymentLink(1L, 2L, BigDecimal.TEN);

        assertThat(link).isEqualTo("http://payment.link/123");

        verify(httpClient).send(any(), any());
    }

    @Test
    void testCreatePaymentLink_failure() throws Exception {
        when(httpClient.send(any(), any())).thenThrow(new RuntimeException("fail"));

        assertThatThrownBy(() -> service.createPaymentLink(1L, 2L, BigDecimal.ONE))
                .isInstanceOf(ReservationException.class);
    }
}
