package com.example.SportsEquipmentRental.service;

import com.example.SportsEquipmentRental.model.Rental;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface RentalService {
    @Transactional
    Rental rent(String equipmentId, String userId);

    boolean isEquipmentRented(String equipmentId);

    Optional<Rental> findActiveRentalByEquipmentId(String equipmentId);

    boolean returnRental(String equipmentId, String userId);

    List<Rental> findAll();
}
