package com.example.SportsEquipmentRental.repository;

import com.example.SportsEquipmentRental.model.AgeGroup;
import com.example.SportsEquipmentRental.model.Equipment;
import com.example.SportsEquipmentRental.model.TypeOfEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EquipmentRepository  extends JpaRepository<Equipment,String> {
    List<Equipment> findByIsActiveTrue();
    Optional<Equipment> findByIdAndIsActiveTrue(String id);
    List<Equipment> findByIsActiveTrueAndIdNotIn(Set<String> rentedEquipmentIds);

    List<Equipment> findByTypeOfEquipment(TypeOfEquipment type);

    List<Equipment> findByAgeGroupAndIsActiveTrue(AgeGroup ageGroup);
    List<Equipment> findByTypeOfEquipmentAndIsActiveTrue(TypeOfEquipment typeOfEquipment);

    @Query("SELECT e.typeOfEquipment AS type, COUNT(e) AS cnt " +
            "FROM Equipment e " +
            "GROUP BY e.typeOfEquipment")
    List<TypeCount> countByType();

    interface TypeCount {
        TypeOfEquipment getType();
        long getCnt();
    }


}
