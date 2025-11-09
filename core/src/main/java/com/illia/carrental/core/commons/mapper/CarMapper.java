package com.illia.carrental.core.commons.mapper;

import com.illia.carrental.core.data.entity.Car;
import com.illia.carrental.core.data.entity.CarReview;
import com.illia.carrental.core.model.dto.CarDTO;
import com.illia.carrental.core.model.dto.CarReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDTO toCarDTO(Car car);

    CarReviewDTO toCarReviewDTO(CarReview review);
}