package com.example.SportsEquipmentRental.service.impl;

import com.example.SportsEquipmentRental.model.AgeGroup;
import com.example.SportsEquipmentRental.model.Equipment;
import com.example.SportsEquipmentRental.model.EquipmentTypeStats;
import com.example.SportsEquipmentRental.model.TypeOfEquipment;
import com.example.SportsEquipmentRental.repository.EquipmentRepository;
import com.example.SportsEquipmentRental.repository.RentalRepository;
import com.example.SportsEquipmentRental.service.EquipmentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.SportsEquipmentRental.repository.EquipmentRepository.TypeCount;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final RentalRepository rentalRepository;
    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, RentalRepository rentalRepository) {
        this.equipmentRepository = equipmentRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipment> findAll(){
        return equipmentRepository.findAll();
    }

    @Override
    public List<Equipment> findAllActive() {
        return equipmentRepository.findByIsActiveTrue();
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @Override
    public Optional<Equipment> findById(String id) {
        return equipmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Equipment save(Equipment equipment) {
        if (equipment.getId() == null || equipment.getId().isBlank()) {
            equipment.setId(UUID.randomUUID().toString());
            equipment.setActive(true);
        }
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return savedEquipment;
    }
    @Override
    public List<Equipment> findByType(TypeOfEquipment type) {
        return equipmentRepository.findByTypeOfEquipment(type);
    }
    @Override
    public List<Equipment> findAvailableEquipment() {
        Set<String> rentedIds=rentalRepository.findRentedEquipmentIds();
        return equipmentRepository.findByIsActiveTrueAndIdNotIn(rentedIds);
    }
    @Override
    public List<Equipment> findRentedEquipment() {
        Set<String> rentedIds = rentalRepository.findRentedEquipmentIds();
        return equipmentRepository.findAllById(rentedIds);
    }

    @Override
    public boolean isAvailable(String equipmentId) {
        return equipmentRepository.findByIdAndIsActiveTrue(equipmentId).isPresent() && !rentalRepository.existsByEquipmentIdAndReturnDateIsNull(equipmentId);
    }

    @Override
    public void deleteById(String id) {
        equipmentRepository.findById(id).ifPresent(e -> {e.setActive(false);
            equipmentRepository.save(e);
        });
    }
    @Override
    public List<Equipment> findByAgeGroup(AgeGroup ageGroup) {
        return equipmentRepository.findByAgeGroupAndIsActiveTrue(ageGroup);
    }

    @Override
    public Map<TypeOfEquipment, Long> countByType() {
        return equipmentRepository.countByType()
                .stream()
                .collect(Collectors.toMap(
                        TypeCount::getType,
                        TypeCount::getCnt
                ));
    }

    @Override
    public EquipmentTypeStats statsByType(TypeOfEquipment type) {
        List<Equipment> allOfType = equipmentRepository
                .findByTypeOfEquipmentAndIsActiveTrue(type);

        Set<String> rentedIds = rentalRepository.findRentedEquipmentIds();

        List<Equipment> rentedItems = allOfType.stream()
                .filter(e -> rentedIds.contains(e.getId()))
                .toList();

        long total = allOfType.size();
        long rented = rentedItems.size();
        long available = total - rented;

        return new EquipmentTypeStats(type, total, rented, available, rentedItems);
    }
}