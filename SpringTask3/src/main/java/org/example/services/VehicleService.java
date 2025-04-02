package org.example.services;

import org.example.models.Vehicle;
import org.example.repositories.impl.VehicleJsonRepository;

import java.util.List;

public class VehicleService {

    private final VehicleJsonRepository vehicleJsonRepository;

    public VehicleService(VehicleJsonRepository vehicleJsonRepository){
        //TODO  // Możesz tu sprawdzić, czy nie ma już pojazdu o takiej samej tablicy rejestracyjnej, itd. – jeśli chcesz.
        this.vehicleJsonRepository = vehicleJsonRepository;
    }

    public Vehicle addVehicle(Vehicle vehicle){
        return vehicleJsonRepository.save(vehicle);
    }

    public void removeVehicle(String vehicleId){
        vehicleJsonRepository.deleteById(vehicleId);
    }

    public List<Vehicle> getAllVehicles(){
        return vehicleJsonRepository.findAll();
    }
    public Vehicle getVehicleById(String id) {
        return vehicleJsonRepository.findById(id).orElse(null);
    }
}
