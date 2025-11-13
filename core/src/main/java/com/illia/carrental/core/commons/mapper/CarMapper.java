package com.illia.carrental.core.commons.mapper;

import com.illia.carrental.core.data.entity.Car;
import com.illia.carrental.core.data.entity.CarRentalDetails;
import com.illia.carrental.core.data.entity.CarReview;
import com.illia.carrental.core.model.dto.CarDTO;
import com.illia.carrental.core.model.dto.CarReviewDTO;
import com.illia.carrental.core.model.dto.RentalDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "carRentalDetails", target = "rentalDetails")
    CarDTO toCarDTO(Car car);

    CarReviewDTO toCarReviewDTO(CarReview review);

    default RentalDetailsDTO toRentalDetailsDTO(CarRentalDetails carRentalDetails) {
        if (carRentalDetails == null) {
            return null;
        }
        return new RentalDetailsDTO(carRentalDetails.getRate());
    }
}
