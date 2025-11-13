package com.illia.carrental.core.api._public;

import com.illia.carrental.core.commons.annotation.CurrentUser;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.CreateReservationRequest;
import com.illia.carrental.core.model.dto.response.GetReservationsResponse;
import com.illia.carrental.core.model.dto.response.ReserveCarResponse;
import com.illia.carrental.core.service.CarReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/reservations")
@RequiredArgsConstructor
public class ReservationsController {

    private final CarReservationService carReservationService;

    @GetMapping("")
    public ResponseEntity<GetReservationsResponse> getReservations(@CurrentUser UserDTO user) {
        return ResponseEntity.ok(carReservationService.getReservationsByUser(user));
    }

    /**
     * returns payment link
     */
    @PostMapping("")
    public ResponseEntity<ReserveCarResponse> createReservation(@RequestBody CreateReservationRequest createReservationRequest,
                                                                @CurrentUser UserDTO user) {
        return ResponseEntity.ok(carReservationService.createReservation(user, createReservationRequest));
    }

    @PostMapping("/{reservation_id}/release")
    public ResponseEntity<Void> releaseReservation(@PathVariable(name = "reservation_id") Long reservationId,
                                                   @CurrentUser UserDTO user) {
        carReservationService.releaseReservation(reservationId, user.id());
        return ResponseEntity.noContent().build();
    }
}
