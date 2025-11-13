package com.illia.carrental.core.model.dto.response;

import com.illia.carrental.core.model.dto.CarReservationDTO;

import java.util.List;

public record GetReservationsResponse(List<CarReservationDTO> reservations) {
}
