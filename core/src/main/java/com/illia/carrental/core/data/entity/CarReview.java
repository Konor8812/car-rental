package com.illia.carrental.core.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_review_seq")
    @SequenceGenerator(name = "car_review_seq", sequenceName = "car_review_seq", allocationSize = 1)
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "comment")
    private String comment;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "car_id")
    private Long carId;

}
