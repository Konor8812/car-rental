package com.illia.carrental.core.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "car_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_reservation_seq")
    @SequenceGenerator(name = "car_reservation_seq", sequenceName = "car_reservation_seq", allocationSize = 1)
    private Long id;

    private Long carId;

    private Long userId;

    private String status;

    private Instant validUntil;

    private Instant createdAt;

}
