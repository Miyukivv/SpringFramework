package com.example.SportsEquipmentRental.service;

import com.example.SportsEquipmentRental.model.AgeGroup;
import com.example.SportsEquipmentRental.model.Equipment;
import com.example.SportsEquipmentRental.model.EquipmentTypeStats;
import com.example.SportsEquipmentRental.model.TypeOfEquipment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface EquipmentService {
    @Transactional(readOnly = true)
    List<Equipment> findAll();
    List<Equipment> findAllActive();

    @Transactional
    Equipment save(Equipment equipment);

    List<Equipment> findAvailableEquipment();


    @Transactional(readOnly = true)
    Optional<Equipment> findById(String id);

    List<Equipment> findByType(TypeOfEquipment type);

    List<Equipment> findRentedEquipment();

    boolean isAvailable(String equipmentId);

    void deleteById(String id);

    List<Equipment> findByAgeGroup(AgeGroup ageGroup);

    Map<TypeOfEquipment, Long> countByType();

    EquipmentTypeStats statsByType(TypeOfEquipment type);


}
