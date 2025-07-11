package com.example.SportsEquipmentRental.service.impl;


import com.example.SportsEquipmentRental.model.Equipment;
import com.example.SportsEquipmentRental.model.Rental;
import com.example.SportsEquipmentRental.model.User;
import com.example.SportsEquipmentRental.repository.EquipmentRepository;
import com.example.SportsEquipmentRental.repository.RentalRepository;
import com.example.SportsEquipmentRental.repository.UserRepository;
import com.example.SportsEquipmentRental.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {

    EquipmentRepository equipmentRepository;
    UserRepository userRepository;
    RentalRepository rentalRepository;

    public RentalServiceImpl(EquipmentRepository equipmentRepository, UserRepository userRepository, RentalRepository rentalRepository) {
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public Rental rent(String vehicleId, String userId) {
        if (isEquipmentRented(vehicleId)){
            throw new IllegalStateException("Equipment " + vehicleId + " is not available for rent.");
        }
        Equipment equipment= equipmentRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Equipment consistency error. ID: " + vehicleId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });
        Rental newRental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .equipment(equipment)
                .user(user)
                .rentDate(LocalDateTime.now().toString())
                .returnDate(null)
                .build();
        Rental savedRental = rentalRepository.save(newRental);
        return savedRental;
    }

    @Override
    public boolean isEquipmentRented(String vehicleId) {
        return rentalRepository.existsByEquipmentIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public Optional<Rental> findActiveRentalByEquipmentId(String vehicleId) {
        return rentalRepository.findByEquipmentIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    @Transactional
    public boolean returnRental(String vehicleId, String userId) {
        Optional <Rental> opt = rentalRepository.findByEquipmentIdAndUserIdAndReturnDateIsNull(vehicleId,userId);
        if (opt.isPresent()){
            Rental rental = opt.get();
            rental.setReturnDate(LocalDateTime.now().toString());
            return true;
        }

        return false;
    }
    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
