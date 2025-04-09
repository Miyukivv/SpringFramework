package org.example.services;

import org.example.models.Rental;
import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    public VehicleService(VehicleRepository vehicleRepository, RentalRepository rentalRepository){
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    public Vehicle addVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public void removeVehicle(String vehicleId){
        vehicleRepository.deleteById(vehicleId);
    }

    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }
    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> all = vehicleRepository.findAll();
        List<Rental> rentals = rentalRepository.findAll();

        Set<String> rentedIds = rentals.stream()
                .filter(r -> r.getReturnDateTime() == null)
                .map(Rental::getVehicleId)
                .collect(Collectors.toSet());

        return all.stream()
                .filter(v -> !rentedIds.contains(v.getId()))
                .toList();
    }
}
