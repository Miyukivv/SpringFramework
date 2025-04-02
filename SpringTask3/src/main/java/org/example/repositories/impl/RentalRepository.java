package org.example.repositories.impl;

import com.google.gson.reflect.TypeToken;
import org.example.models.Rental;
import org.example.repositories.IRentalRepository;
import org.example.utils.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalRepository implements IRentalRepository {
    private final JsonFileStorage<Rental> storage = new JsonFileStorage<>("src/main/resources/rentals.json", new TypeToken<List<Rental>>(){}.getType());

    private final List<Rental> rentals;

    public RentalRepository() {
        this.rentals = new ArrayList<>(storage.load());
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Rental> findByVehicleId(String id) {
        return rentals.stream().filter(r -> r.getVehicleId().equals(id)).findFirst();
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        return rentals.stream().filter(r -> r.getUserId().equals(userId)).findFirst();
    }

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null || rental.getId().isBlank()) {
            rental.setId(UUID.randomUUID().toString());
        } else {
            deleteById(rental.getId());
        }
        rentals.add(rental);
        storage.save(rentals);
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(v -> v.getId().equals(id));
        storage.save(rentals);
    }
}
