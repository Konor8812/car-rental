package com.illia.carrental.payments.service;

import com.illia.carrental.payments.config.CarRentalCoreConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CarRentalCoreNotificatorTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private CarRentalCoreConfig coreConfig;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private CarRentalCoreNotificator notificator;

    @Captor
    private ArgumentCaptor<HttpRequest> requestCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================================
    // Successful notification (2xx response)
    // ============================================================================
    @Test
    void testNotifyReservationCompleted_successful() throws Exception {
        when(coreConfig.confirmReservationUrl())
                .thenReturn("https://core/api/reservations/%s/confirm");

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        assertDoesNotThrow(() ->
                notificator.notifyReservationCompleted(123L)
        );

        verify(httpClient).send(requestCaptor.capture(), any());
        var req = requestCaptor.getValue();

        // URL built correctly
        assert req.uri().toString().equals("https://core/api/reservations/123/confirm");

        // POST method
        assert req.method().equals("POST");
    }

    // ============================================================================
    // Error (>=400) should not throw
    // ============================================================================
    @Test
    void testNotifyReservationCompleted_errorStatus() throws Exception {
        when(coreConfig.confirmReservationUrl())
                .thenReturn("https://core/api/reservations/%s/confirm");

        when(httpResponse.statusCode()).thenReturn(500);
        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        assertDoesNotThrow(() ->
                notificator.notifyReservationCompleted(99L)
        );

        verify(httpClient).send(any(), any());
    }

    // ============================================================================
    // HttpClient throws – should not throw out of the method
    // ============================================================================
    @Test
    void testNotifyReservationCompleted_exceptionDuringSend() throws Exception {
        when(coreConfig.confirmReservationUrl())
                .thenReturn("https://core/api/reservations/%s/confirm");

        when(httpClient.send(any(), any()))
                .thenThrow(new RuntimeException("Network error"));

        assertDoesNotThrow(() ->
                notificator.notifyReservationCompleted(55L)
        );

        verify(httpClient).send(any(), any());
    }

    // ============================================================================
    // buildUrl() formatting test
    // ============================================================================
    @Test
    void testBuildUrlFormatting() throws Exception {
        when(coreConfig.confirmReservationUrl())
                .thenReturn("http://local-core/res/%s/ok");

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        notificator.notifyReservationCompleted(777L);

        verify(httpClient).send(requestCaptor.capture(), any());
        var request = requestCaptor.getValue();

        assert request.uri().toString().equals("http://local-core/res/777/ok");
    }
}
