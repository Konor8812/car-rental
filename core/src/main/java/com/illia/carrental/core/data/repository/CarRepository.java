package com.illia.carrental.core.data.repository;

import com.illia.carrental.core.data.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "SELECT c FROM Car c LEFT JOIN FETCH c.carRentalDetails",
            countQuery = "SELECT COUNT(c) FROM Car c")
    Page<Car> findByIdWithRentailDetailsLoaded(Pageable pageable);

    @Query(value = "SELECT c FROM Car c LEFT JOIN FETCH c.carRentalDetails WHERE c.id = :carId")
    Optional<Car> findByIdWithRentailDetailsLoaded(Long carId);
}
