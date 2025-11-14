package com.illia.carrental.payments.service.impl;

import com.illia.carrental.payments.commons.enums.PaymentStatus;
import com.illia.carrental.payments.data.entity.Payment;
import com.illia.carrental.payments.data.repository.PaymentRepository;
import com.illia.carrental.payments.dto.request.CompletePaymentRequest;
import com.illia.carrental.payments.dto.request.CreatePaymentLinkRequest;
import com.illia.carrental.payments.service.CarRentalCoreNotificator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentsServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CarRentalCoreNotificator coreNotificator;

    @InjectMocks
    private PaymentsServiceImpl paymentsService;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================================
    // createPaymentLink
    // ============================================================================

    @Test
    void testCreatePaymentLink_success() {
        // Given
        CreatePaymentLinkRequest request = new CreatePaymentLinkRequest(
                1L,
                10L,
                new BigDecimal("150.00")
        );

        // When
        var response = paymentsService.createPaymentLink(request);

        // Then
        assertNotNull(response);
        assertTrue(response.link().startsWith("/payments/"));

        verify(paymentRepository).save(paymentCaptor.capture());
        var savedPayment = paymentCaptor.getValue();

        assertEquals(1L, savedPayment.getUserId());
        assertEquals(10L, savedPayment.getReservationId());
        assertEquals(new BigDecimal("150.00"), savedPayment.getTotal());
        assertEquals(PaymentStatus.PENDING, savedPayment.getStatus());
        assertNotNull(savedPayment.getExternalPaymentId());
        assertNotNull(savedPayment.getCreatedAt());
        assertNotNull(savedPayment.getUpdatedAt());
    }

    // ============================================================================
    // completePayment (successful)
    // ============================================================================

    @Test
    void testCompletePayment_successful() {
        // Given
        CompletePaymentRequest request = new CompletePaymentRequest(
                "ext-123",
                true
        );

        Payment payment = Payment.builder()
                .id(5L)
                .reservationId(88L)
                .status(PaymentStatus.PENDING)
                .externalPaymentId("ext-123")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(paymentRepository.findByExternalPaymentId("ext-123"))
                .thenReturn(Optional.of(payment));

        // When
        paymentsService.completePayment(request);

        // Then
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        verify(paymentRepository).save(payment);
        verify(coreNotificator).notifyReservationCompleted(88L);
    }

    // ============================================================================
    // completePayment (failed)
    // ============================================================================

    @Test
    void testCompletePayment_failed() {
        // Given
        CompletePaymentRequest request = new CompletePaymentRequest(
                "ext-321",
                false
        );

        Payment payment = Payment.builder()
                .id(7L)
                .reservationId(22L)
                .status(PaymentStatus.PENDING)
                .externalPaymentId("ext-321")
                .build();

        when(paymentRepository.findByExternalPaymentId("ext-321"))
                .thenReturn(Optional.of(payment));

        // When
        paymentsService.completePayment(request);

        // Then
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        verify(paymentRepository).save(payment);
        verify(coreNotificator, never()).notifyReservationCompleted(any());
    }

    // ============================================================================
    // completePayment (not found)
    // ============================================================================

    @Test
    void testCompletePayment_notFound() {
        when(paymentRepository.findByExternalPaymentId("missing"))
                .thenReturn(Optional.empty());

        var request = new CompletePaymentRequest("missing", true);

        assertThrows(IllegalArgumentException.class,
                () -> paymentsService.completePayment(request));

        verify(paymentRepository, never()).save(any());
        verify(coreNotificator, never()).notifyReservationCompleted(any());
    }
}
