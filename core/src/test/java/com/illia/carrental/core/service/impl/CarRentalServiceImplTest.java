package com.illia.carrental.core.service.impl;

import com.illia.carrental.core.commons.mapper.CarMapper;
import com.illia.carrental.core.data.entity.Car;
import com.illia.carrental.core.data.entity.CarRentalDetails;
import com.illia.carrental.core.data.repository.CarRepository;
import com.illia.carrental.core.data.repository.CarReviewRepository;
import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.service.CarRentalService;
import com.illia.carrental.core.service.CarReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CarRentalServiceImplTest {

    private CarRepository carRepository;
    private CarReservationService carReservationService;
    private CarMapper carMapper;
    private CarReviewRepository carReviewRepository;

    private CarRentalService service;

    @BeforeEach
    void setup() {
        carRepository = mock(CarRepository.class);
        carReservationService = mock(CarReservationService.class);
        carMapper = mock(CarMapper.class);
        carReviewRepository = mock(CarReviewRepository.class);

        service = new CarRentalServiceImpl(
                carRepository,
                carReservationService,
                carMapper,
                carReviewRepository
        );
    }

    @Test
    void testGetAllCars() {
        Car car = new Car();
        Page<Car> page = new PageImpl<>(List.of(car));

        when(carRepository.findByIdWithRentailDetailsLoaded(any(Pageable.class))).thenReturn(page);
        when(carMapper.toCarDTO(any())).thenReturn(null);

        GetAllCarsResponse result = service.getAllCars(0, 10);

        assertThat(result.page()).isEqualTo(0);
        assertThat(result.pageSize()).isEqualTo(1);
        assertThat(result.cars()).hasSize(1);
    }

    @Test
    void testGetCarInfo_available() {
        Car car = new Car();
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carReservationService.existsByCarIdAndStatusIn(anyLong(), any()))
                .thenReturn(false);

        when(carMapper.toCarDTO(car)).thenReturn(null);

        GetCarInfoResponse resp = service.getCarInfo(1L);

        assertThat(resp.available()).isTrue();
    }

    @Test
    void testGetCarInfo_notFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCarInfo(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testAddReview() {
        UserDTO user = new UserDTO(10L, "a", "b");
        AddReviewRequest req = new AddReviewRequest(5, "nice");

        service.addReview(user, 7L, req);

        ArgumentCaptor<com.illia.carrental.core.data.entity.CarReview> captor =
                ArgumentCaptor.forClass(com.illia.carrental.core.data.entity.CarReview.class);

        verify(carReviewRepository).save(captor.capture());
        var saved = captor.getValue();

        assertThat(saved.getCarId()).isEqualTo(7L);
        assertThat(saved.getUserId()).isEqualTo(10L);
        assertThat(saved.getScore()).isEqualTo(5);
        assertThat(saved.getComment()).isEqualTo("nice");
    }

    @Test
    void testGetCurrentRentRate() {
        CarRentalDetails details = new CarRentalDetails();
        details.setRate(BigDecimal.TEN);

        Car car = new Car();
        car.setCarRentalDetails(details);

        when(carRepository.findByIdWithRentailDetailsLoaded(5L))
                .thenReturn(Optional.of(car));

        var result = service.getCurrentRentRate(5L);

        assertThat(result.rate()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void testGetCurrentRentRate_notFound() {
        when(carRepository.findByIdWithRentailDetailsLoaded(5L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCurrentRentRate(5L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
