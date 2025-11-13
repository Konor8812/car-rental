package com.illia.carrental.payments.data.repository;

import com.illia.carrental.payments.data.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByExternalPaymentId(String externalPaymentId);
}
