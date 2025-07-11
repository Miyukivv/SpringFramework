package com.example.SportsEquipmentRental.repository;

import com.example.SportsEquipmentRental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RentalRepository extends JpaRepository<Rental,String> {
    Optional<Rental> findByEquipmentIdAndReturnDateIsNull(String equipmentId);
    Optional<Rental> findByEquipmentIdAndUserIdAndReturnDateIsNull(String equipmentId, String userId);
    boolean existsByEquipmentIdAndReturnDateIsNull(String equipmentId);

    @Query("SELECT r.equipment.id FROM Rental r WHERE r.returnDate IS NULL")
    Set<String> findRentedEquipmentIds();
}
