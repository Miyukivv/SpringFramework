package com.example.car_rent.service;

import com.example.car_rent.model.Vehicle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface VehicleService {
    @Transactional(readOnly = true)
    List<Vehicle> findAll();

    List<Vehicle> findAllActive();
    Optional<Vehicle> findById(String id);

    @Transactional
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAvailableVehicles();
    List<Vehicle> findRentedVehicles();

    boolean isAvailable(String vehicleId);
    void deleteById(String id);


}
