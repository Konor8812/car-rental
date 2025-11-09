package com.illia.carrental.core.service;

import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;

public interface CarRentalService {

    GetAllCarsResponse getAllCars(Integer page, Integer size);

    GetCarInfoResponse getCarInfo(Long carId);

    ReserveCarResponse reserveCar(UserDTO user, Long carId);

    void releaseCar(Long userId, Long carId);

    void addReview(UserDTO user, Long carId, AddReviewRequest request);

    void completeReservation(Long reservationId, UserDTO user);
}
