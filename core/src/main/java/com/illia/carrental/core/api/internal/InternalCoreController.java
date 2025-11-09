package com.illia.carrental.core.api.internal;

import com.illia.carrental.core.commons.annotation.CurrentUser;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalCoreController {

    private final CarRentalService carRentalService;

    @PostMapping("/reservations/{reservation_id}/completed")
    public ResponseEntity<Void> rentCar(@PathVariable(name = "reservation_id") Long reservationId,
                                        @CurrentUser UserDTO user) {
        carRentalService.completeReservation(reservationId, user);
        return ResponseEntity.noContent().build();
    }
}
