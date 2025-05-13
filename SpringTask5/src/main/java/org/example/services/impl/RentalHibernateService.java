package org.example.services.impl;

import org.example.HibernateConfig;
import org.example.repositories.impl.RentalHibernateRepository;
import org.example.repositories.impl.UserHibernateRepository;
import org.example.repositories.impl.VehicleHibernateRepository;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.IRentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RentalHibernateService implements IRentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo, UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;
        Session session = null;
        try {

            session = HibernateConfig.getSessionFactory().openSession();
            tx = session.beginTransaction();

            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);

            if (rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()) {
                throw new IllegalStateException("Pojazd jest już wypożyczony");
            }

            Vehicle vehicle = vehicleRepo.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Pojazd nie znaleziony"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDate(LocalDateTime.now().toString())
                    .returnDate(null)
                    .build();

            rentalRepo.save(rental);
            tx.commit();
            return rental;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);

            Rental rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Brak aktywnego wypożyczenia tego pojazdu"));

            if (!rental.getUser().getId().equals(userId)) {
                throw new IllegalStateException("Ten pojazd nie był wypożyczony przez tego użytkownika");
            }

            rental.setReturnDate(LocalDateTime.now().toString());
            rentalRepo.save(rental);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Rental> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            rentalRepo.setSession(session);
            return rentalRepo.findAll();
        }
    }

    // RentalHibernateService.java
    public List<Vehicle> findActiveVehiclesByUserId(String userId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT r.vehicle FROM Rental r " +
                                    "WHERE r.user.id = :uid AND r.returnDate IS NULL",
                            Vehicle.class)
                    .setParameter("uid", userId)
                    .list();
        }
    }
}
