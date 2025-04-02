package org.example.db;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.util.List;
import java.util.Optional;

public class RentalJdbcRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        return null;
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByVehicleId(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
