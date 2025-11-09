package com.illia.carrental.core.model.dto.response;

import com.illia.carrental.core.model.dto.CarDTO;

public record GetCarInfoResponse(CarDTO car,
                                 Boolean available) {
}
