package com.example.car_rent.service.impl;

import com.example.car_rent.model.Vehicle;
import com.example.car_rent.repository.RentalRepository;
import com.example.car_rent.repository.VehicleRepository;
import com.example.car_rent.service.VehicleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }


    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    @Transactional //otwiera transakcje na poczatku metody, zamyka (zatwierdza lub wycofuje) transakcje po zakonczeniu metody (commit lub rollback)
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return savedVehicle;
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        Set<String> rentedIds=rentalRepository.findRentedVehicleIds();
        List<Vehicle> resultList=vehicleRepository.findByIsActiveTrueAndIdNotIn(rentedIds);
        return resultList;
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        Set<String> rentedIds = rentalRepository.findRentedVehicleIds();
        return vehicleRepository.findAllById(rentedIds);
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return vehicleRepository.findByIdAndIsActiveTrue(vehicleId).isPresent() && !rentalRepository.existsByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public void deleteById(String id) {
        vehicleRepository.findById(id).ifPresent(v -> {v.setActive(false);
            vehicleRepository.save(v);
        });
    }
}

