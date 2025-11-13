package com.illia.carrental.core.data.repository;

import com.illia.carrental.core.data.entity.CarReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarReservationRepository extends JpaRepository<CarReservation, Long> {

    boolean existsByCarIdAndStatusIn(Long carId, List<String> awaitsPayment);

    Optional<CarReservation> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE CarReservation cr SET cr.status = :status WHERE cr.id = :reservationId")
    void updateStatusByIdAndUserId(Long reservationId, String status);

    @Query("FROM CarReservation cr JOIN FETCH cr.car WHERE cr.userId = :userId")
    List<CarReservation> getAllByUserId(Long userId);
}
