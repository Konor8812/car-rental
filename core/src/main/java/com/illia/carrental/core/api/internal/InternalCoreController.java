package com.illia.carrental.core.api.internal;

import com.illia.carrental.core.service.CarReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/internal")
@RequiredArgsConstructor
public class InternalCoreController {

    private final CarReservationService carReservationService;

    @PostMapping("/reservations/{reservation_id}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable(name = "reservation_id") Long reservationId) {
        carReservationService.completeReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
