package com.illia.carrental.core.api._public;

import com.illia.carrental.core.commons.annotation.CurrentUser;
import com.illia.carrental.core.model.dto.AddReviewRequest;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import com.illia.carrental.core.model.dto.response.GetCarInfoResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;
import com.illia.carrental.core.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoreController {

    private final CarRentalService carRentalService;

    @GetMapping("/cars")
    public ResponseEntity<GetAllCarsResponse> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(carRentalService.getAllCars(page, size));
    }

    @GetMapping("/cars/{car_id}")
    public ResponseEntity<GetCarInfoResponse> getCarInfo(@PathVariable(name = "car_id") Long carId) {
        return ResponseEntity.ok(carRentalService.getCarInfo(carId));
    }

    /**
     * returns payment link
     */
    @PostMapping("/cars/{car_id}/reserve")
    public ResponseEntity<ReserveCarResponse> reserveCar(@PathVariable(name = "car_id") Long carId,
                                                      @CurrentUser UserDTO user) {
        return ResponseEntity.ok(carRentalService.reserveCar(user, carId));
    }

    @PostMapping("/cars/{car_id}/release")
    public ResponseEntity<Void> releaseCar(@PathVariable(name = "car_id") Long carId,
                                           @CurrentUser UserDTO user) {
        carRentalService.releaseCar(user.id(), carId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cars/{car_id}/review")
    public ResponseEntity<Void> addReview(@PathVariable(name = "car_id") Long carId,
                                          @RequestBody AddReviewRequest request,
                                          @CurrentUser UserDTO user) {
        carRentalService.addReview(user, carId, request);
        return ResponseEntity.noContent().build();
    }
}
