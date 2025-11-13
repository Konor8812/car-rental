package com.illia.carrental.core.model.dto;

import java.util.List;

public record CarDTO(Long id,
                     String type,
                     String manufacturer,
                     String model,
                     Integer year,
                     RentalDetailsDTO rentalDetails,
                     List<CarReviewDTO> reviews) {
}
