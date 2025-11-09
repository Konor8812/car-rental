package com.illia.carrental.core.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "car_rental_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRentalDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_rental_details_seq")
    @SequenceGenerator(name = "car_rental_details_seq", sequenceName = "car_rental_details_seq", allocationSize = 1)
    private Long id;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;


    @OneToOne(mappedBy = "details", cascade = CascadeType.ALL)
    private Car car;

}