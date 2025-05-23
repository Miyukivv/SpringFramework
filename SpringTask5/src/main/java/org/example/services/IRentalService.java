package org.example.services;

import org.example.models.Rental;
import org.example.models.Vehicle;

import java.util.List;

public interface IRentalService {
    boolean isVehicleRented(String vehicleId);
    Rental rent(String vehicleId, String userId);
    boolean returnRental(String vehicleId, String userId);
    List<Rental> findAll();
    List<Vehicle> findActiveVehiclesByUserId(String userId);
}
