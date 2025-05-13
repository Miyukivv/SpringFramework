package org.example.services.impl;

import org.example.HibernateConfig;
import org.example.repositories.impl.RentalHibernateRepository;
import org.example.repositories.impl.VehicleHibernateRepository;
import org.example.models.Rental;
import org.example.models.Vehicle;
import org.example.services.IVehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VehicleHibernateService implements IVehicleService {
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;


    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo, RentalHibernateRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id);
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            vehicleRepo.setSession(session);
            Vehicle saved = vehicleRepo.save(vehicle);
            session.getTransaction().commit();
            return saved;
        }
    }
    public boolean deleteById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Vehicle vehicle = session.get(Vehicle.class, id);
            if (vehicle == null) {
                return false;
            }

            session.remove(vehicle);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Vehicle> findAvailableVehicles() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);

            List<Vehicle> allVehicles = vehicleRepo.findAll();
            List<Rental> rentals = rentalRepo.findAll();

            Set<String> rentedIds = rentals.stream()
                    .filter(r -> r.getReturnDate() == null)
                    .map(r -> r.getVehicle().getId())
                    .collect(Collectors.toSet());

            return allVehicles.stream()
                    .filter(v -> !rentedIds.contains(v.getId()))
                    .toList();
        }
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isEmpty();
        }
    }
}