package com.illia.carrental.core.model.dto.response;

import com.illia.carrental.core.model.dto.CarDTO;

import java.util.List;

public record GetAllCarsResponse(List<CarDTO> cars,
                                 Integer page,
                                 Integer pageSize,
                                 Long totalElements) {
}
