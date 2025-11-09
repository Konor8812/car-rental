package com.illia.carrental.core.service.impl;

import com.illia.carrental.core.commons.exception.CarReservationException;
import com.illia.carrental.core.commons.mapper.CarMapper;
import com.illia.carrental.core.data.entity.CarReservation;
import com.illia.carrental.core.data.entity.CarReview;
import com.illia.carrental.core.data.repository.CarRepository;
import com.illia.carrental.core.data.repository.CarReservationRepository;
import com.illia.carrental.core.data.repository.CarReviewRepository;
import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;
import com.illia.carrental.core.service.CarRentalService;
import com.illia.carrental.core.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.illia.carrental.core.commons.constant.CarReservationStatusStatusConstants.*;

@RequiredArgsConstructor
@Service
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRepository carRepository;
    private final CarReservationRepository carReservationRepository;
    private final PaymentService paymentService;
    private final CarMapper carMapper;
    private final CarReviewRepository carReviewRepository;

    @Override
    public GetAllCarsResponse getAllCars(Integer page, Integer size) {
        var carPage = carRepository.findAll(PageRequest.of(page, size));

        var carDTOS = carPage.getContent().stream()
                .map(carMapper::toCarDTO)
                .toList();

        return new GetAllCarsResponse(
                carDTOS,
                carPage.getNumber(),
                carPage.getSize(),
                carPage.getTotalElements()
        );
    }

    @Override
    public GetCarInfoResponse getCarInfo(Long carId) {
        var car = carRepository.findById(carId)
                .orElseThrow(EntityNotFoundException::new);
        var activeReservationExists = carReservationRepository.existsByCarIdAndStatusIn(
                carId, List.of(AWAITS_PAYMENT, CONFIRMED));

        var carDto = carMapper.toCarDTO(car);
        return new GetCarInfoResponse(carDto,
                !activeReservationExists);
    }

    @Override
    @Transactional
    public ReserveCarResponse reserveCar(UserDTO user, Long carId) {
        var activeReservationExists = carReservationRepository.existsByCarIdAndStatusIn(
                carId, List.of(AWAITS_PAYMENT, CONFIRMED));

        if (activeReservationExists) {
            throw new CarReservationException("Car already reserved or rented");
        }

        var reservation = CarReservation.builder()
                .carId(carId)
                .userId(user.id())
                .status(AWAITS_PAYMENT)
                .createdAt(Instant.now())
                .validUntil(Instant.now().plusSeconds(900)) // 15 min
                .build();
        carReservationRepository.save(reservation);

        var paymentLink = paymentService.createPaymentLink(user.accountId(), carId);
        return new ReserveCarResponse(paymentLink, AWAITS_PAYMENT);

    }

    @Override
    @Transactional
    public void releaseCar(Long userId, Long carId) {
        var reservation = carReservationRepository.findByCarIdAndUserId(carId, userId)
                .orElseThrow(EntityNotFoundException::new);
        carReservationRepository.updateStatusByIdAndUserId(
                reservation.getId(),
                userId,
                CANCELED);
    }

    @Override
    public void addReview(UserDTO user, Long carId, AddReviewRequest request) {
        var review = CarReview.builder()
                .userId(user.id())
                .carId(carId)
                .score(request.score())
                .comment(request.comment())
                .build();
        carReviewRepository.save(review);
    }

    @Override
    @Transactional
    public void completeReservation(Long reservationId, UserDTO user) {
        carReservationRepository.updateStatusByIdAndUserId(reservationId, user.id(), CONFIRMED);

    }
}
