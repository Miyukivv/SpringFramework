package com.example.car_rent.service;

import com.example.car_rent.model.Rental;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface RentalService {
    @Transactional
    Rental rent(String vehicleId, String userId);
    boolean isVehicleRented(String vehicleId);
    Optional<Rental> findActiveRentalByVehicleId(String vehicleId);

    boolean returnRental(String vehicleId, String userId);
    List<Rental> findAll();

}
