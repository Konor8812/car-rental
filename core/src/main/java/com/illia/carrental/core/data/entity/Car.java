package com.illia.carrental.core.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq")
    @SequenceGenerator(name = "car_seq", sequenceName = "car_seq", allocationSize = 1)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year")
    private Integer year;

    @OneToMany(mappedBy = "car")
    private List<CarReview> reviews;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_rentail_details_id")
    private CarRentalDetails carRentalDetails;

}