package com.illia.carrental.core.data.repository;

import com.illia.carrental.core.data.entity.Car;
import com.illia.carrental.core.model.dto.response.GetAllCarsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

}
