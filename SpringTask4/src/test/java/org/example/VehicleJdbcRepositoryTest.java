package org.example;


import org.example.models.Vehicle;
import org.example.repositories.VehicleRepository;
import org.example.repositories.dblImpl.VehicleJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class VehicleJdbcRepositoryTest {

    private VehicleRepository repository;

    @BeforeEach
    public void setup() {
        repository = new VehicleJdbcRepository();
    }

    @Test
    public void testInsertAndFindVehicleById() {
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID().toString())
                .category("SUV")
                .brand("Toyota")
                .model("RAV4")
                .year(2020)
                .plate("XYZ123")
                .price(150.0)
                .build();

        repository.save(vehicle);

        Optional<Vehicle> result = repository.findById(vehicle.getId());
        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().getBrand());
    }
    @Test
    public void testUpdateVehicleById() {
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID().toString())
                .category("Hatchback")
                .brand("Honda")
                .model("Civic")
                .year(2018)
                .plate("ABC789")
                .price(120.0)
                .build();

        repository.save(vehicle);

        vehicle.setModel("Civic LX");
        vehicle.setPrice(125.0);
        repository.save(vehicle);

        Optional<Vehicle> updated = repository.findById(vehicle.getId());
        assertTrue(updated.isPresent());
        assertEquals("Civic LX", updated.get().getModel());
        assertEquals(125.0, updated.get().getPrice());
    }
}