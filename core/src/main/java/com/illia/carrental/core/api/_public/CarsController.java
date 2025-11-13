package com.illia.carrental.core.api._public;

import com.illia.carrental.core.commons.annotation.CurrentUser;
import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.GetRateResponse;
import com.illia.carrental.core.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/cars")
@RequiredArgsConstructor
public class CarsController {

    private final CarRentalService carRentalService;

    @GetMapping("")
    public ResponseEntity<GetAllCarsResponse> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(carRentalService.getAllCars(page, size));
    }

    @GetMapping("/{car_id}")
    public ResponseEntity<GetCarInfoResponse> getCarInfo(@PathVariable(name = "car_id") Long carId) {
        return ResponseEntity.ok(carRentalService.getCarInfo(carId));
    }

    @PostMapping("/{car_id}/rate")
    public ResponseEntity<GetRateResponse> getCurrentRentRate(@PathVariable(name = "car_id") Long carId) {
        return ResponseEntity.ok(carRentalService.getCurrentRentRate(carId));
    }

    @PostMapping("/{car_id}/review")
    public ResponseEntity<Void> addReview(@PathVariable(name = "car_id") Long carId,
                                          @RequestBody AddReviewRequest request,
                                          @CurrentUser UserDTO user) {
        carRentalService.addReview(user, carId, request);
        return ResponseEntity.noContent().build();
    }

}
