package org.example.db;

import org.example.models.Vehicle;
import org.example.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleJdbcRepository implements VehicleRepository {
    @Override
    public List<Vehicle> findAll() {
        return null;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
