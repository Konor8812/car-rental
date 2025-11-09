package com.illia.carrental.core.data.repository;

import com.illia.carrental.core.data.entity.Car;
import com.illia.carrental.core.data.entity.CarReservation;
import jakarta.persistence.OneToMany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarReservationRepository extends JpaRepository<CarReservation, Long> {

    Optional<CarReservation> findByCarIdAndUserId(Long carId, Long userId);

    boolean existsByCarIdAndStatusIn(Long carId, List<String> awaitsPayment);

    Optional<CarReservation> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE CarReservation cr SET cr.status = :status WHERE cr.id = :reservationId AND cr.userId = :userId")
    void updateStatusByIdAndUserId(Long reservationId, Long userId, String status);
}
