package com.illia.carrental.core.service;

import com.illia.carrental.core.data.entity.CarReservation;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.CreateReservationRequest;
import com.illia.carrental.core.model.dto.response.GetReservationsResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;

import java.util.List;

public interface CarReservationService {

    GetReservationsResponse getReservationsByUser(UserDTO user);

    ReserveCarResponse createReservation(UserDTO user, CreateReservationRequest request);

    void completeReservation(Long reservationId);

    void releaseReservation(Long reservationId, Long userId);

    boolean existsByCarIdAndStatusIn(Long carId, List<String> awaitsPayment);

    CarReservation saveReservation(CarReservation reservation);
}
