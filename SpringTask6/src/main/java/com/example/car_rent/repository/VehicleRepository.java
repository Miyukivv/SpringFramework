package com.example.car_rent.repository;

import com.example.car_rent.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository  extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByIsActiveTrue();
    Optional<Vehicle> findByIdAndIsActiveTrue(String id);
    @Query("SELECT v FROM Vehicle v WHERE v.isActive = true AND v.id NOT IN :rentedIds")
    List<Vehicle> findByIsActiveTrueAndIdNotIn(@Param("rentedIds")Set<String> rentedVehicleIds);
}
