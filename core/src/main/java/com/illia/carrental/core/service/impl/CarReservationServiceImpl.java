package com.illia.carrental.core.service.impl;

import com.illia.carrental.core.commons.exception.CarReservationException;
import com.illia.carrental.core.commons.mapper.CarMapper;
import com.illia.carrental.core.data.entity.CarReservation;
import com.illia.carrental.core.data.repository.CarReservationRepository;
import com.illia.carrental.core.model.dto.CarReservationDTO;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.CreateReservationRequest;
import com.illia.carrental.core.model.dto.response.GetReservationsResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;
import com.illia.carrental.core.service.CarReservationService;
import com.illia.carrental.core.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.illia.carrental.core.commons.constant.CarReservationStatusStatusConstants.*;

@Service
@RequiredArgsConstructor
public class CarReservationServiceImpl implements CarReservationService {

    private final CarReservationRepository carReservationRepository;
    private final PaymentService paymentService;
    private final CarMapper carMapper;

    @Override
    public GetReservationsResponse getReservationsByUser(UserDTO user) {
        var mappedReservations = carReservationRepository
                .getAllByUserId(user.id())
                .stream()
                .map(reservation ->
                        new CarReservationDTO(
                                reservation.getId(),
                                carMapper.toCarDTO(reservation.getCar()),
                                reservation.getStatus(),
                                reservation.getCreatedAt(),
                                reservation.getValidUntil()))
                .toList();
        return new GetReservationsResponse(mappedReservations);
    }

    @Override
    public ReserveCarResponse createReservation(UserDTO user, CreateReservationRequest request) {
        var activeReservationExists = existsByCarIdAndStatusIn(
                request.carId(), List.of(AWAITS_PAYMENT, CONFIRMED));

        if (activeReservationExists) {
            throw new CarReservationException("Car is already reserved or rented :(");
        }

        var reservation = CarReservation.builder()
                .carId(request.carId())
                .userId(user.id())
                .status(AWAITS_PAYMENT)
                .createdAt(Instant.now())
                .validUntil(Instant.now().plusSeconds(900)) // 15 min
                .numberOfDays(request.numOfDays())
                .build();
        reservation = saveReservation(reservation);

        var paymentLink = paymentService.createPaymentLink(
                user.id(),
                reservation.getId(),
                request.total());
        return new ReserveCarResponse(paymentLink, AWAITS_PAYMENT);
    }


    @Override
    @Transactional
    public void completeReservation(Long reservationId) {
        var reservation = carReservationRepository.findById(reservationId)
                .orElseThrow(EntityNotFoundException::new);
        if (!reservation.getCreatedAt().isBefore(Instant.now())) {
            throw new CarReservationException("Car reservation is invalid");
        }
        var validUntil = Instant.now().plusSeconds(reservation.getNumberOfDays() * 24 * 60 * 60);
        reservation.setStatus(CONFIRMED);
        reservation.setValidUntil(validUntil);
        carReservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void releaseReservation(Long reservationId, Long userId) {
        var reservation = carReservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(EntityNotFoundException::new);
        carReservationRepository.updateStatusByIdAndUserId(reservation.getId(), CANCELED);
    }

    @Override
    public boolean existsByCarIdAndStatusIn(Long carId, List<String> statuses) {
        return carReservationRepository.existsByCarIdAndStatusIn(carId, statuses);
    }

    @Override
    public CarReservation saveReservation(CarReservation reservation) {
        return carReservationRepository.save(reservation);
    }
}
