package com.illia.carrental.core.service;

import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.GetRateResponse;

public interface CarRentalService {

    GetAllCarsResponse getAllCars(Integer page, Integer size);

    GetCarInfoResponse getCarInfo(Long carId);

    void addReview(UserDTO user, Long carId, AddReviewRequest request);

    GetRateResponse getCurrentRentRate(Long carId);

}
