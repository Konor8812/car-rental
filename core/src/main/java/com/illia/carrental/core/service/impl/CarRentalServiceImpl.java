package com.illia.carrental.core.service.impl;

import com.illia.carrental.core.commons.mapper.CarMapper;
import com.illia.carrental.core.data.entity.CarReview;
import com.illia.carrental.core.data.repository.CarRepository;
import com.illia.carrental.core.data.repository.CarReviewRepository;
import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.GetRateResponse;
import com.illia.carrental.core.service.CarRentalService;
import com.illia.carrental.core.service.CarReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.illia.carrental.core.commons.constant.CarReservationStatusStatusConstants.*;

@RequiredArgsConstructor
@Service
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRepository carRepository;
    private final CarReservationService carReservationService;
    private final CarMapper carMapper;
    private final CarReviewRepository carReviewRepository;

    @Override
    public GetAllCarsResponse getAllCars(Integer page, Integer size) {
        var carPage = carRepository.findByIdWithRentailDetailsLoaded(PageRequest.of(page, size));

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
        var activeReservationExists = carReservationService.existsByCarIdAndStatusIn(
                carId, List.of(AWAITS_PAYMENT, CONFIRMED));

        var carDto = carMapper.toCarDTO(car);
        return new GetCarInfoResponse(carDto,
                !activeReservationExists);
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
    public GetRateResponse getCurrentRentRate(Long carId) {
        var car = carRepository.findByIdWithRentailDetailsLoaded(carId)
                .orElseThrow(EntityNotFoundException::new);
        return new GetRateResponse(car.getCarRentalDetails().getRate());
    }

}
